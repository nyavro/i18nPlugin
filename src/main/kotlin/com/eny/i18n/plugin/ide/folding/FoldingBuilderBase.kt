package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.factory.LanguageFactory
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParser
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PropertyReference
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.LocalizationSourceSearch
import com.eny.i18n.plugin.utils.ellipsis
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement

internal data class ElementToReferenceBinding(val psiElement: PsiElement, val reference: PropertyReference<PsiElement>)

/**
 * Provides folding mechanism for i18n keys
 */
abstract class FoldingBuilderBase(private val languageFactory: LanguageFactory) : FoldingBuilderEx(), DumbAware, CompositeKeyResolver<PsiElement> {

    private val group = FoldingGroup.newGroup("i18n")

    private val parser: KeyParser = KeyParser()

    override fun getPlaceholderText(node: ASTNode): String? = ""

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val config = Settings.getInstance(root.project).config()
        if (!config.foldingEnabled) return arrayOf()
        val search = LocalizationSourceSearch(root.project)
        val foldingProvider = languageFactory.foldingProvider()
        return foldingProvider.collectContainers(root)
            .flatMap { container ->
                val (literals, offset) = foldingProvider.collectLiterals(container)
                literals.mapNotNull { literal ->
                    parser
                        .parse(literal.text.unQuote(), config.nsSeparator, config.keySeparator, config.vue)
                        ?.let { key -> resolve(container, literal, search, config, key) }
                        ?.let { resolved ->
                            FoldingDescriptor(
                                container.node,
                                foldingProvider.getFoldingRange(container, offset, resolved.psiElement),
                                group,
                                resolved.reference.element?.value()?.text?.unQuote()?.ellipsis(config.foldingMaxLength) ?: ""
                            )
                        }
                }
            }.toTypedArray()
    }

    private fun resolve(container: PsiElement, element: PsiElement, search: LocalizationSourceSearch, config: Config, fullKey: FullKey): ElementToReferenceBinding? {
        return search
            .findFilesByHost(fullKey.allNamespaces(), container)
            .filter {
                if (config.vue) it.name.contains(config.foldingPreferredLanguage)
                else it.parent == config.foldingPreferredLanguage
            }
            .map { resolveCompositeKey(fullKey.compositeKey, PsiElementTree.create(it.element)) }
            .firstOrNull { it.unresolved.isEmpty() && it.element?.isLeaf() == true }
            ?.let { ElementToReferenceBinding(element, it) }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = true
}

