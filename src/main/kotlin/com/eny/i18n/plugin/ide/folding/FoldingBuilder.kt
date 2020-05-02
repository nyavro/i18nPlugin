package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PropertyReference
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.*
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

internal data class ElementToReferenceBinding(val psiElement: PsiElement, val reference: PropertyReference<PsiElement>)

/**
 * Provides folding mechanism for i18n keys
 */
abstract class FoldingBuilderBase<C: PsiElement>(
        private val callExpressionClass: Class<C>,
        private val collectFoldingContainers: (root: PsiElement) -> List<PsiElement>,
        private val collectLiterals: (container: PsiElement) -> Pair<List<PsiElement>, Int> = fun (container: PsiElement) = Pair(listOf(container), 0)
) : FoldingBuilderEx(), DumbAware, CompositeKeyResolver<PsiElement> {

    private val parser: ExpressionKeyParser = ExpressionKeyParser()

    override fun getPlaceholderText(node: ASTNode): String? = ""

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val settings = Settings.getInstance(root.project)
        if (!settings.foldingEnabled) return arrayOf()
        val search = LocalizationSourceSearch(root.project)
        val group = FoldingGroup.newGroup("i18n")
        return collectFoldingContainers(root)
            .flatMap { container ->
                val (literals, offset) = collectLiterals(container)
                literals.mapNotNull { literal ->
                    parser
                        .parse(literal.text.unQuote(), settings.nsSeparator, settings.keySeparator, settings.stopCharacters)
                        ?.let { key -> resolve(literal, search, settings, key) }
                        ?.let { resolved ->
                            val callElement = PsiTreeUtil.getParentOfType(resolved.psiElement, callExpressionClass) ?: resolved.psiElement
                            val placeholder = resolved.reference.element?.value()?.text?.unQuote()?.ellipsis(settings.foldingMaxLength) ?: ""
                            FoldingDescriptor(
                                container.node,
                                callElement.textRange.shiftRight(
                                    if (container != literal) (container.textOffset + offset) else 0
                                ),
                                group,
                                placeholder
                            )
                        }
                }
            }.toTypedArray()
    }

    private fun resolve(element: PsiElement, search: LocalizationSourceSearch, settings: Settings, fullKey: FullKey): ElementToReferenceBinding? {
        return search
            .findFilesByName(fullKey.ns?.text)
            .filter {
                if (settings.vue) it.name.contains(settings.foldingPreferredLanguage)
                else it.parent == settings.foldingPreferredLanguage
            }
            .map { resolveCompositeKey(fullKey.compositeKey, PsiElementTree.create(it.element)) }
            .firstOrNull { it.unresolved.isEmpty() }
            ?.let { ElementToReferenceBinding(element, it) }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = true
}

