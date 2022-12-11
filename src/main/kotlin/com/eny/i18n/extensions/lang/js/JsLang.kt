package com.eny.i18n.extensions.lang.js

import com.eny.i18n.Lang
import com.eny.i18n.extensions.lang.js.extractors.*
import com.eny.i18n.plugin.factory.FoldingProvider
import com.eny.i18n.plugin.factory.TranslationExtractor
import com.eny.i18n.plugin.parser.RawKey
import com.intellij.psi.PsiElement

open class JsLang : Lang {

    override fun canExtractKey(element: PsiElement): Boolean {
        return extractRawKey(element) != null
//        return listOf(
//            ReactUseTranslationHookExtractor(),
//            TemplateKeyExtractor(),
//            LiteralKeyExtractor(),
//            StringLiteralKeyExtractor(),
//            XmlAttributeKeyExtractor()
//        )
//            .any {it.canExtract(element)}
//        return listOf("JS:STRING_LITERAL", "JS:LITERAL_EXPRESSION", "JS:STRING_TEMPLATE_PART", "JS:STRING_TEMPLATE_EXPRESSION")
//            .contains(element.type()) && (
//                JSPatterns.jsArgument("t", 0).let { pattern ->
//                    pattern.accepts(element) ||
//                            pattern.accepts(PsiTreeUtil.findFirstParent(element, { it.parent?.type() == "JS:ARGUMENT_LIST" }))
//                } ||
//                        JSPatterns.jsArgument("\$t", 0).let { pattern ->
//                            pattern.accepts(element) ||
//                                    pattern.accepts(PsiTreeUtil.findFirstParent(element, { it.parent?.type() == "JS:ARGUMENT_LIST" }))
//                        } ||
//                        XmlPatterns.xmlAttributeValue("i18nKey").accepts(element) ||
//                        element.parents(false).toList()
//                            .mapNotNull {(it as? JSProperty)?.name ?: (it as? ES6Property)?.computedPropertyName?.let {it.expression?.reference?.resolve() as? TypeScriptEnumField }?.name}
//                            .reversed().joinToString(Settings.getInstance(element.project).config().keySeparator).endsWith(element.text.unQuote())
//                )
    }

    override fun extractRawKey(element: PsiElement): RawKey? {
        return listOf(
                    ReactUseTranslationHookExtractor(),
                    TemplateKeyExtractor(),
                    LiteralKeyExtractor(),
                    StringLiteralKeyExtractor(),
                    XmlAttributeKeyExtractor()
            ).find {it.canExtract(element)}?.extract(element)
        }

    override fun foldingProvider(): FoldingProvider = JsFoldingProvider()

    override fun translationExtractor(): TranslationExtractor = JsTranslationExtractor()
}