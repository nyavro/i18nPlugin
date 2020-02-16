package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.ExpressionKeyParser
import com.eny.i18n.plugin.utils.FullKey
import com.intellij.psi.PsiElement

/**
 * Gets element's type string
 */
fun PsiElement.type(): String = this.node?.elementType.toString()

/**
 * Extracts translation key from psi element
 */
class FullKeyExtractor {

    private val parser: ExpressionKeyParser = ExpressionKeyParser()
    /**
     * Converts element to it's literal value, if possible
     */
    fun extractI18nKeyLiteral(element: PsiElement): FullKey? {
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