package com.eny.i18n.plugin.language.jsx

import com.eny.i18n.plugin.factory.*
import com.eny.i18n.plugin.ide.settings.i18NextSettings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.plugin.parser.XmlAttributeKeyExtractor
import com.eny.i18n.plugin.utils.toBoolean
import com.intellij.lang.ecmascript6.JSXHarmonyFileType
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.lang.javascript.TypeScriptJSXFileType
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlTag

/**
 * Vue language components factory
 */
class JsxLanguageFactory: LanguageFactory {
    override fun translationExtractor(): TranslationExtractor = JsxTranslationExtractor()
    /**
     * No special implementation for folding builder is required
     */
    override fun foldingProvider(): FoldingProvider = object: FoldingProvider {
        override fun collectContainers(root: PsiElement): List<PsiElement> = emptyList()
        override fun collectLiterals(container: PsiElement): Pair<List<PsiElement>, Int> = Pair(emptyList(), 0)
        override fun getFoldingRange(container: PsiElement, offset: Int, psiElement: PsiElement): TextRange = TextRange.EMPTY_RANGE
    }

    override fun callContext() = object: CallContext {
        override fun accepts(element: PsiElement): Boolean = false
    }

    override fun referenceAssistant(): ReferenceAssistant = JsxReferenceAssistant()
}

class JsxReferenceAssistant: ReferenceAssistant {

    override fun pattern(): ElementPattern<out PsiElement> {
        return XmlPatterns.xmlAttributeValue("i18nKey")
    }
    override fun extractKey(element: PsiElement): FullKey? {
        val config = element.project.i18NextSettings()
        val parser = KeyParserBuilder
            .withSeparators(config.nsSeparator, config.keySeparator)
            .withTemplateNormalizer()
            .build()
        return listOf(
            XmlAttributeKeyExtractor()
        )
            .find {it.canExtract(element)}
            ?.let {parser.parse(it.extract(element))}
    }
}

internal class JsxTranslationExtractor: TranslationExtractor {
    override fun canExtract(element: PsiElement): Boolean =
        listOf(JSXHarmonyFileType.INSTANCE, TypeScriptJSXFileType.INSTANCE).any { it == element.containingFile.fileType } &&
            !PsiTreeUtil.findChildOfType(PsiTreeUtil.getParentOfType(element, XmlTag::class.java), XmlTag::class.java).toBoolean()

    override fun isExtracted(element: PsiElement): Boolean =
        element.isJs() && JSPatterns.jsArgument("t", 0).accepts(element.parent)

    override fun text(element: PsiElement): String =
        PsiTreeUtil.getParentOfType(element, XmlTag::class.java)!!
            .value
            .textElements
            .map {it.text}
            .joinToString(" ")

    override fun textRange(element: PsiElement): TextRange =
        PsiTreeUtil.getParentOfType(element, XmlTag::class.java)!!
            .value
            .textElements
            .let {
                TextRange(
                    it.first().textRange.startOffset,
                    it.last().textRange.endOffset
                )
            }

    override fun template(element: PsiElement): (argument: String) -> String = {"{i18n.t($it)}"}
    private fun PsiElement.isJs(): Boolean = this.language == JavascriptLanguage.INSTANCE
}