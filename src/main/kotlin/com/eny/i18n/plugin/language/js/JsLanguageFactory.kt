package com.eny.i18n.plugin.language.js

import com.eny.i18n.plugin.factory.*
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.FullKeyExtractor
import com.eny.i18n.plugin.key.parser.KeyParser
import com.eny.i18n.plugin.parser.*
import com.eny.i18n.plugin.utils.default
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
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
    override fun accepts(element: PsiElement): Boolean = JSPatterns.jsArgument("t", 0).accepts(element)
}

internal class JsReferenceAssistant: ReferenceAssistant {

    private val parser: KeyParser = KeyParser()

    override fun pattern(): ElementPattern<out PsiElement> = JSPatterns.jsLiteralExpression()

    override fun extractKey(element: PsiElement): FullKey? {
        val settings = Settings.getInstance(element.project)
        return listOf(
            TemplateKeyExtractor(),
            LiteralKeyExtractor()
        )
            .find {it.canExtract(element)}
            ?.let {parser.parse(it.extract(element), settings.nsSeparator, settings.keySeparator)}
    }
}