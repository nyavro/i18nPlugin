package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement

/**
 * Extracts i18n key from js string literal
 */
class StringLiteralKeyExtractor: KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean = listOf("JS:STRING_LITERAL", "quoted string", "String").any{
        item ->
            element.type().contains(item)
    }

    override fun extract(element: PsiElement, parser: ExpressionKeyParser, settings: Settings): FullKey? =
        parser.parse(
            listOf(KeyElement.literal(element.text.unQuote())),
            false,
            settings.nsSeparator,
            settings.keySeparator,
            settings.stopCharacters,
            emptyNamespace = settings.vue
        )
}