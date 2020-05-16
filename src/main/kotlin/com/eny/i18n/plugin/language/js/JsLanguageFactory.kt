package com.eny.i18n.plugin.language.js

import com.eny.i18n.plugin.factory.LanguageFactory
import com.eny.i18n.plugin.factory.extractor.TranslationExtractor
import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.psi.PsiElement

/**
 * Vue language components factory
 */
class JsLanguageFactory: LanguageFactory {
    override fun translationExtractor(): TranslationExtractor = JsTranslationExtractor()
}

internal class JsTranslationExtractor: TranslationExtractor {
    override fun canExtract(element: PsiElement): Boolean = "JS:STRING_LITERAL" == element.type()
    override fun isExtracted(element: PsiElement): Boolean = JSPatterns.jsArgument("t", 0).accepts(element.parent)
    override fun text(element: PsiElement): String = element.text.unQuote()
}