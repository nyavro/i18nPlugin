package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.factory.CallContext
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.FullKey
import com.intellij.psi.PsiElement

/**
 * Gets element's type string
 */
fun PsiElement.type(): String = this.node?.elementType.toString()

interface Extractor {
    fun extractI18nKeyLiteral(element: PsiElement): FullKey?
}

class DummyContext: CallContext {
    override fun accepts(element: PsiElement): Boolean = true
}

/**
 *
 */
class FullKeyExtractor(val context: CallContext, val extractor: Extractor): Extractor {
    override fun extractI18nKeyLiteral(element: PsiElement): FullKey? =
        if (context.accepts(element)) extractor.extractI18nKeyLiteral(element)
        else null
}

/**
 * Extracts translation key from psi element
 */
class KeyExtractorImpl: Extractor {

    private val parser: ExpressionKeyParser = ExpressionKeyParser()
    /**
     * Converts element to it's literal value, if possible
     */
    override fun extractI18nKeyLiteral(element: PsiElement): FullKey? {
        val settings = Settings.getInstance(element.project)
        val extractors = listOf(
            TemplateKeyExtractor(),
            LiteralKeyExtractor(),
            StringLiteralKeyExtractor(),
            TemplatePartKeyExtractor()
        )
        return extractors.find { extractor -> extractor.canExtract(element) }?.extract(element, parser, settings)
    }
}