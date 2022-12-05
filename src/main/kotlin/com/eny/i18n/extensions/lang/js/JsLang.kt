package com.eny.i18n.extensions.lang.js

import com.eny.i18n.Lang
import com.eny.i18n.plugin.factory.CallContext
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.parser.*
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.ecmascript6.psi.ES6Property
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSProperty
import com.intellij.lang.javascript.psi.ecma6.TypeScriptEnumField
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parents

open class JsLang : Lang {

    override fun canExtractKey(element: PsiElement): Boolean {
        return extractFullKey(element) != null
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

    override fun extractFullKey(element: PsiElement): FullKey? {
        return KeyExtractorImpl().extractFullKey(element)
    }
}