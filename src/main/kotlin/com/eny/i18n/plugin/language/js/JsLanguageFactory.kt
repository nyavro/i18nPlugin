package com.eny.i18n.plugin.language.js

import com.eny.i18n.plugin.factory.LanguageFactory
import com.eny.i18n.plugin.factory.TranslationExtractor
import com.eny.i18n.plugin.utils.type
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.psi.PsiElement

/**
 * Js language components factory
 */
class JsLanguageFactory: LanguageFactory {
    override fun translationExtractor(): TranslationExtractor = JsTranslationExtractor()
}

internal class JsTranslationExtractor: TranslationExtractor {
    override fun canExtract(element: PsiElement): Boolean = "JS:STRING_LITERAL" == element.type()
            && element.containingFile.language.isKindOf(JavascriptLanguage.INSTANCE)
    override fun isExtracted(element: PsiElement): Boolean = JSPatterns.jsArgument("t", 0).accepts(element.parent)
    override fun text(element: PsiElement): String = element.text.unQuote()
}

