package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.plugin.key.CompositeKeyResolver
import com.eny.i18n.plugin.key.PropertyReference
import com.eny.i18n.plugin.utils.unQuote
import com.eny.i18n.plugin.addons.technology.vue.VueSettings
import com.eny.i18n.plugin.addons.technology.vue.vueSettings
import com.eny.i18n.plugin.ide.*
import com.eny.i18n.plugin.ide.settings.CommonSettings
import com.eny.i18n.plugin.ide.settings.commonSettings
import com.eny.i18n.plugin.key.KeyElement
import com.eny.i18n.plugin.utils.ellipsis
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.eny.i18n.plugin.ide.settings.i18NextSettings as i18NextSettings1

internal data class ElementToReferenceBinding(val psiElement: PsiElement, val reference: PropertyReference<PsiElement, LocalizationType>)

/**
 * Provides folding mechanism for i18n keys
 */
abstract class FoldingBuilderBase(private val languageFactory: LanguageFactory) : FoldingBuilderEx(), DumbAware, CompositeKeyResolver<PsiElement, LocalizationType> {

    private val group = FoldingGroup.newGroup("i18n")

    override fun getPlaceholderText(node: ASTNode): String? = ""

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val vueSettings = root.project.vueSettings()
//        val poSettings = root.project.poSettings()
        val i18NextSettings = root.project.i18NextSettings1()
        val commonSettings = root.project.commonSettings()
        val gettext = false//poSettings.gettext
        val parser = (
            if (gettext) KeyParserBuilder.withoutTokenizer()
            else KeyParserBuilder.withSeparators(i18NextSettings.nsSeparator, i18NextSettings.keySeparator).withNormalizer(ExpressionNormalizer())
        ).build()
        if (!commonSettings.foldingEnabled) return arrayOf()
        val search = LocalizationSourceSearch(root.project)
        val foldingProvider = languageFactory.foldingProvider()
        return foldingProvider.collectContainers(root)
            .flatMap { container ->
                val (literals, offset) = foldingProvider.collectLiterals(container)
                literals.mapNotNull { literal ->
                    parser.parse(Pair(listOf(KeyElement.literal(literal.text.unQuote())), null) , vueSettings.vue || gettext)
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

