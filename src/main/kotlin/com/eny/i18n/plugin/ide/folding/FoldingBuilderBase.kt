package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.Extensions
import com.eny.i18n.Lang
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.plugin.parser.RawKey
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PropertyReference
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.LocalizationSourceService
import com.eny.i18n.plugin.utils.ellipsis
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement

internal data class ElementToReferenceBinding(val psiElement: PsiElement, val reference: PropertyReference)

/**
 * Provides folding mechanism for i18n keys
 */
abstract class FoldingBuilderBase(private val lang: Lang) : FoldingBuilderEx(), DumbAware, CompositeKeyResolver<PsiElement> {

    private val group = FoldingGroup.newGroup("i18n")

    override fun getPlaceholderText(node: ASTNode): String? = ""

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val config = Settings.getInstance(root.project).config()
        val parser = (
            if (config.gettext) KeyParserBuilder.withoutTokenizer()
            else KeyParserBuilder.withSeparators(config.nsSeparator, config.keySeparator).withTemplateNormalizer()
        ).build()
        if (!config.foldingEnabled) return arrayOf()
        val foldingProvider = lang.foldingProvider()
        return foldingProvider.collectContainers(root, Extensions.TECHNOLOGY.extensionList.flatMap {it.translationFunctions()})
            .flatMap { container ->
                listOf(container).mapNotNull { literal ->
                    val rawKey = lang.extractRawKey(literal)
                    rawKey?.let {rk ->
                        parser.parse(rk, config.gettext, config.firstComponentNs)
                            ?.let { key -> resolve(container, literal, config, key) }
                            ?.let { resolved ->
                                FoldingDescriptor(
                                    container.node,
                                    foldingProvider.getFoldingRange(container, 0, resolved.psiElement),
                                    group,
                                    resolved.reference.element?.value()?.text?.unQuote()
                                        ?.ellipsis(config.foldingMaxLength) ?: ""
                                )
                            }
                    }
                }
            }.toTypedArray()
    }

    private fun resolve(container: PsiElement, element: PsiElement, config: Config, fullKey: FullKey): ElementToReferenceBinding? {
        return element.project.service<LocalizationSourceService>()
            .findSources(fullKey.allNamespaces(), container.project)
            .filter {
                it.parent == config.foldingPreferredLanguage
            }
            .map { resolveCompositeKey(fullKey.keyPrefix + fullKey.compositeKey, it)}
            .firstOrNull { it.unresolved.isEmpty() && it.element?.isLeaf() == true }
            ?.let { ElementToReferenceBinding(element, it) }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = true
}

