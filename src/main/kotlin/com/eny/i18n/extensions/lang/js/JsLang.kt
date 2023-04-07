package com.eny.i18n.extensions.lang.js

import com.eny.i18n.Lang
import com.eny.i18n.TranslationFunction
import com.eny.i18n.extensions.lang.js.extractors.*
import com.eny.i18n.plugin.factory.FoldingProvider
import com.eny.i18n.plugin.factory.TranslationExtractor
import com.eny.i18n.plugin.parser.RawKey
import com.eny.i18n.plugin.utils.type
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

open class JsLang : Lang {

    override fun canExtractKey(element: PsiElement, translationFunctions: List<TranslationFunction>): Boolean {
        return translationFunctions.any {
            (name, argumentIndex) -> JSPatterns.jsArgument(name, argumentIndex).let { pattern ->
                pattern.accepts(element) ||
                        pattern.accepts(PsiTreeUtil.findFirstParent(element, { it.parent?.type() == "JS:ARGUMENT_LIST" }))
            }
        }  && extractRawKey(element) != null
//                JSPatterns.jsArgument("\$t", 0).let { pattern ->
//                            pattern.accepts(element) ||
//                                    pattern.accepts(PsiTreeUtil.findFirstParent(element, { it.parent?.type() == "JS:ARGUMENT_LIST" }))
//                } ||
//                XmlPatterns.xmlAttributeValue("i18nKey").accepts(element) ||
//                element.parents(false).toList()
//                        .mapNotNull {(it as? JSProperty)?.name ?: (it as? ES6Property)?.computedPropertyName?.let {it.expression?.reference?.resolve() as? TypeScriptEnumField }?.name}
//                        .reversed()
//                    .joinToString(Settings.getInstance(element.project).config().keySeparator).endsWith(element.text.unQuote())
    }

    override fun extractRawKey(element: PsiElement): RawKey? {
        val extractor = listOf(
            ReactUseTranslationHookExtractor(),
            TemplateKeyExtractor(),
            LiteralKeyExtractor(),
            StringLiteralKeyExtractor(),
            XmlAttributeKeyExtractor()
        ).find {it.canExtract(element)}
        return extractor?.extract(element)
    }

    override fun foldingProvider(): FoldingProvider = JsFoldingProvider()

    override fun translationExtractor(): TranslationExtractor = JsTranslationExtractor()

    override fun resolveLiteral(entry: PsiElement): PsiElement? {
        val typeName = entry.node.elementType.toString()
        return if (setOf("JS:STRING_LITERAL", "JS:STRING_TEMPLATE_EXPRESSION").contains (typeName)) entry
            else if (typeName == "JS:STRING_TEMPLATE_PART") entry.parent
            else null
    }
}
