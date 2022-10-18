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
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.ElementPatternCondition
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

/**
 * Vue language components factory
 */
class JsLanguageFactory: LanguageFactory {
    override fun translationExtractor(): TranslationExtractor = JsTranslationExtractor()
    override fun foldingProvider(): FoldingProvider = JsFoldingProvider()
    override fun callContext(): CallContext = JsCallContext()
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

internal class JsCallContext: CallContext {
    override fun accepts(element: PsiElement): Boolean =
        listOf("JS:STRING_LITERAL", "JS:LITERAL_EXPRESSION", "JS:STRING_TEMPLATE_PART", "JS:STRING_TEMPLATE_EXPRESSION")
            .contains(element.type()) &&
            JSPatterns.jsArgument("t", 0).let { pattern ->
                pattern.accepts(element) ||
                pattern.accepts(PsiTreeUtil.findFirstParent(element, { it.parent?.type() == "JS:ARGUMENT_LIST" }))
            } ||
            //TODO: what about vue $t function?
            JSPatterns.jsArgument("\$t", 0).let { pattern ->
                pattern.accepts(element) ||
                    pattern.accepts(PsiTreeUtil.findFirstParent(element, { it.parent?.type() == "JS:ARGUMENT_LIST" }))
            } ||
            XmlPatterns.xmlAttributeValue("i18nKey").accepts(element)
}

internal class JsReferenceAssistant: ReferenceAssistant {

    override fun pattern(): ElementPattern<out PsiElement> =
        object : ElementPattern<PsiElement> {
            val v = JSPatterns.jsLiteralExpression().andOr(JSPatterns.jsArgument("t", 0), JSPatterns.jsArgument("\$t", 0))
            override fun accepts(o: Any?): Boolean {
                return v.accepts(o)
            }

            override fun accepts(o: Any?, context: ProcessingContext?): Boolean {
                return v.accepts(o, context)
            }

            override fun getCondition(): ElementPatternCondition<PsiElement>? {
                return v.condition as ElementPatternCondition<PsiElement>
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
            ?.let {parser.parse(it.extract(element), emptyNamespace = config.vue, firstComponentNamespace = config.firstComponentNs)}
    }
}
