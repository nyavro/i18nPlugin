package com.eny.i18n.plugin.language.js

import com.eny.i18n.plugin.factory.*
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.plugin.parser.LiteralKeyExtractor
import com.eny.i18n.plugin.parser.ReactUseTranslationHookExtractor
import com.eny.i18n.plugin.parser.TemplateKeyExtractor
import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.default
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.ecmascript6.psi.ES6Property
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.lang.javascript.psi.JSProperty
import com.intellij.lang.javascript.psi.ecma6.TypeScriptEnumField
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.ElementPatternCondition
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parents
import com.intellij.util.ProcessingContext

/**
 * Js language components factory
 */
class JsLanguageFactory: LanguageFactory {
    override fun translationExtractor(): TranslationExtractor = JsTranslationExtractor()
    override fun foldingProvider(): FoldingProvider = JsFoldingProvider()
    override fun referenceAssistant(): ReferenceAssistant = JsReferenceAssistant()
}

internal class JsTranslationExtractor: TranslationExtractor {
    override fun canExtract(element: PsiElement): Boolean = "JS:STRING_LITERAL" == element.type()
            && element.containingFile.language.isKindOf(JavascriptLanguage.INSTANCE)
    override fun isExtracted(element: PsiElement): Boolean = JSPatterns.jsArgument("t", 0).accepts(element.parent)
    override fun text(element: PsiElement): String = element.text.unQuote()
}

internal class JsFoldingProvider: FoldingProvider {
    override fun collectContainers(root: PsiElement): List<PsiElement> =
        PsiTreeUtil
            .findChildrenOfType(root, JSLiteralExpression::class.java)
            .filter {JSPatterns.jsArgument("t", 0).accepts(it)}

    override fun collectLiterals(container: PsiElement): Pair<List<PsiElement>, Int> = Pair(listOf(container), 0)
    override fun getFoldingRange(container: PsiElement, offset: Int, psiElement: PsiElement): TextRange =
        PsiTreeUtil.getParentOfType(psiElement, JSCallExpression::class.java).default(psiElement).textRange
}


internal class JsReferenceAssistant: ReferenceAssistant {

    override fun pattern(): ElementPattern<out PsiElement> =
        object : ElementPattern<PsiElement> {
            private val v = JSPatterns.jsLiteralExpression().andOr(
                JSPatterns.jsArgument("t", 0),
                JSPatterns.jsArgument("\$t", 0),
            )

            private fun isAlias(element: JSLiteralExpression): Boolean {
                val config = Settings.getInstance(element.project).config()
                return element.parents(false).toList()
                    .mapNotNull {(it as? JSProperty)?.name ?: (it as? ES6Property)?.computedPropertyName?.let {it.expression?.reference?.resolve() as? TypeScriptEnumField }?.name}
                    .reversed().joinToString(config.keySeparator).endsWith(element.text.unQuote())
            }

            override fun accepts(o: Any?): Boolean {
                return JSPatterns.jsLiteralExpression().accepts(o) && isAlias(o as JSLiteralExpression) || v.accepts(o)
            }

            override fun accepts(o: Any?, context: ProcessingContext?): Boolean {
                return JSPatterns.jsLiteralExpression().accepts(o) && isAlias(o as JSLiteralExpression) || v.accepts(o, context)
            }

            override fun getCondition(): ElementPatternCondition<PsiElement>? {
                return v.condition as? ElementPatternCondition<PsiElement>
            }
        }


    override fun extractKey(element: PsiElement): FullKey? {
        val config = Settings.getInstance(element.project).config()
        val parser = KeyParserBuilder
            .withSeparators(config.nsSeparator, config.keySeparator)
            .withTemplateNormalizer()
            .build()
        return listOf(
            ReactUseTranslationHookExtractor(),
            TemplateKeyExtractor(),
            LiteralKeyExtractor()
        )
            .find {it.canExtract(element)}
            ?.let {parser.parse(it.extract(element), emptyNamespace = false, firstComponentNamespace = config.firstComponentNs)}
    }
}
