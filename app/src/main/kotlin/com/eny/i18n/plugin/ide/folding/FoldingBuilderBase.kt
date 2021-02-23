package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.ide.annotator.LanguageFactory
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.plugin.key.CompositeKeyResolver
import com.eny.i18n.plugin.key.PropertyReference
import com.eny.i18n.plugin.ide.annotator.PsiElementTree
import com.eny.i18n.plugin.utils.unQuote
import com.eny.i18n.plugin.addons.technology.vue.VueSettings
import com.eny.i18n.plugin.addons.technology.vue.vueSettings
import com.eny.i18n.plugin.ide.annotator.*
import com.eny.i18n.plugin.key.KeyElement
import com.eny.i18n.plugin.parser.ExpressionNormalizer
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

    override fun getPlaceholderText(node: ASTNode): String? = ""

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val vueSettings = root.project.vueSettings()
        val poSettings = root.project.poSettings()
        val i18NextSettings = root.project.i18NextSettings()
        val commonSettings = root.project.commonSettings()
        val parser = (
            if (poSettings.gettext) KeyParserBuilder.withoutTokenizer()
            else KeyParserBuilder.withSeparators(i18NextSettings.nsSeparator, i18NextSettings.keySeparator).withNormalizer(ExpressionNormalizer())
        ).build()
        if (!commonSettings.foldingEnabled) return arrayOf()
        val search = LocalizationSourceSearch(root.project)
        val foldingProvider = languageFactory.foldingProvider()
        return foldingProvider.collectContainers(root)
            .flatMap { container ->
                val (literals, offset) = foldingProvider.collectLiterals(container)
                literals.mapNotNull { literal ->
                    parser.parse(Pair(listOf(KeyElement.literal(literal.text.unQuote())), null) , vueSettings.vue || poSettings.gettext)
                        ?.let { key -> resolve(container, literal, search, commonSettings, key, vueSettings) }
                        ?.let { resolved ->
                            FoldingDescriptor(
                                container.node,
                                foldingProvider.getFoldingRange(container, offset, resolved.psiElement),
                                group,
                                resolved.reference.element?.value()?.text?.unQuote()?.ellipsis(commonSettings.foldingMaxLength) ?: ""
                            )
                        }
                }
            }.toTypedArray()
    }

    private fun resolve(container: PsiElement, element: PsiElement, search: LocalizationSourceSearch, config: CommonSettings, fullKey: FullKey, vueSettings: VueSettings): ElementToReferenceBinding? {
        return search
            .findFilesByHost(fullKey.allNamespaces(), container)
            .filter {
                if (vueSettings.vue) it.name.contains(config.foldingPreferredLanguage)
                else it.parent == config.foldingPreferredLanguage
            }
            .map { resolveCompositeKey(fullKey.compositeKey, PsiElementTree.create(it.element), it.type)}
            .firstOrNull { it.unresolved.isEmpty() && it.element?.isLeaf() == true }
            ?.let { ElementToReferenceBinding(element, it) }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = true
}

