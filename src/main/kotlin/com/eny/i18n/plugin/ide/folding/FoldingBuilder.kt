package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.parser.CaptureContext
import com.eny.i18n.plugin.parser.FullKeyExtractor
import com.eny.i18n.plugin.parser.KeyExtractorImpl
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PropertyReference
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.LocalizationFileSearch
import com.eny.i18n.plugin.utils.ellipsis
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

internal data class ElementToReferenceBinding(val psiElement: PsiElement, val reference: PropertyReference<PsiElement>)

/**
 * Provides folding mechanism for i18n keys
 */
abstract class FoldingBuilderBase<P: PsiElement, L: PsiElement, C: PsiElement, T: PsiElementPattern<P, T>>(
        private val pattern: T,
        private val literalClass: Class<L>,
        private val callExpressionClass: Class<C>) : FoldingBuilderEx(), DumbAware, CompositeKeyResolver<PsiElement> {

    private val extractor = FullKeyExtractor(
        CaptureContext(listOf(pattern)),
        KeyExtractorImpl()
    )

    override fun getPlaceholderText(node: ASTNode): String? {
        val search = LocalizationFileSearch(node.psi.project)
        val settings = Settings.getInstance(node.psi.project)
        val res = resolve(node.psi, search, settings, extractor.extractI18nKeyLiteral(node.psi)!!)?.reference?.element?.value()
        return res?.text?.unQuote()?.ellipsis(settings.foldingMaxLength)
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val settings = Settings.getInstance(root.project)
        if (!settings.foldingEnabled) return arrayOf()
        val search = LocalizationFileSearch(root.project)
        val group = FoldingGroup.newGroup("i18n")
        val descriptors = PsiTreeUtil
            .findChildrenOfType(root, literalClass)
            .filter {pattern.accepts(it)}
            .mapNotNull {
                element -> extractor.extractI18nKeyLiteral(element)
                    ?.let { key -> resolve(element, search, settings, key)}
            }
            .map {
                val item = PsiTreeUtil.getParentOfType(it.psiElement, callExpressionClass) ?: it.psiElement
                FoldingDescriptor(it.psiElement.node, TextRange(item.textRange.startOffset, item.textRange.endOffset), group)
            }
        return descriptors.toTypedArray()
    }

    private fun resolve(element: PsiElement, search: LocalizationFileSearch, settings: Settings, fullKey: FullKey): ElementToReferenceBinding? {
        return search
            .findFilesByName(fullKey.ns?.text)
            .filter { it.parent?.name == settings.foldingPreferredLanguage }
            .map { resolveCompositeKey(fullKey.compositeKey, PsiElementTree.create(it)) }
            .firstOrNull { it.unresolved.isEmpty() }
            ?.let { ElementToReferenceBinding(element, it) }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = true
}

