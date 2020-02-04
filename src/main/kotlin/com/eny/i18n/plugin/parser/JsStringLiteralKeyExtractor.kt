package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.ExpressionKeyParser
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement

/**
 * Extracts i18n key from js string literal
 */
class JsStringLiteralKeyExtractor(): KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean = element.type() == "JS:STRING_LITERAL"

    override fun extract(element: PsiElement, parser: ExpressionKeyParser, normalizer: KeyNormalizer, settings: Settings): FullKey? =
        parser.parse(
            normalizer.normalize(listOf(KeyElement.literal(element.text.unQuote()))),
            false, 
            settings.nsSeparator, 
            settings.keySeparator,
            settings.stopCharacters
        )
}