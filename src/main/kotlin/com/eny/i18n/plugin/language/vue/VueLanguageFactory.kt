package com.eny.i18n.plugin.language.vue

import com.eny.i18n.plugin.factory.LanguageFactory
import com.eny.i18n.plugin.factory.extractor.TranslationExtractor
import com.eny.i18n.plugin.utils.default
import com.eny.i18n.plugin.utils.toBoolean
import com.eny.i18n.plugin.utils.unQuote
import com.eny.i18n.plugin.utils.whenMatches
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.html.HtmlTag
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlToken
import org.jetbrains.vuejs.lang.expr.VueJSLanguage
import org.jetbrains.vuejs.lang.html.VueFileType

/**
 * Vue language components factory
 */
class VueLanguageFactory: LanguageFactory {
    override fun translationExtractor(): TranslationExtractor = VueTranslationExtractor()
}

internal class VueTranslationExtractor: TranslationExtractor {
    override fun canExtract(element: PsiElement): Boolean =
        element.isVueJs() || element.isJs() || element.isVue()

    override fun isExtracted(element: PsiElement): Boolean =
        if (element.isVueJs() || element.isJs()) {
            JSPatterns.jsArgument("\$t", 0).accepts(element.parent)
        } else {
            element.isVue() && element.text.startsWith("\$t")
        }

    override fun text(element: PsiElement): String =
        getTextElement(element).text.unQuote()

    override fun textRange(element: PsiElement): TextRange =
        getTextElement(element).textRange

    override fun template(element: PsiElement): (argument: String) -> String =
        when {
            element.isVueTemplate() -> ({"this.\$t($it)"})
            element.isVue() -> ({"{{ \$t($it) }}"})
            else -> ({"\$t($it)"})
        }

    private fun getTextElement(element: PsiElement): PsiElement =
        if (element.isBorderToken()) {
            element.prevSibling
        } else {
            element.whenMatches { it.isVueText() }?.parent.default(element)
        }
    private fun PsiElement.isJs(): Boolean = this.language == JavascriptLanguage.INSTANCE
    private fun PsiElement.isVueJs(): Boolean = this.language == VueJSLanguage.INSTANCE
    private fun PsiElement.isVue():Boolean = this.containingFile.fileType == VueFileType.INSTANCE
    private fun PsiElement.isVueTemplate():Boolean = this.isVue() && PsiTreeUtil.findFirstParent(this, {it is HtmlTag && it.name == "script"}).toBoolean()
    private fun PsiElement.isVueText(): Boolean = (this is PsiWhiteSpace) || (this is XmlToken)
    private fun PsiElement.isBorderToken(): Boolean = this.text == "</"
}