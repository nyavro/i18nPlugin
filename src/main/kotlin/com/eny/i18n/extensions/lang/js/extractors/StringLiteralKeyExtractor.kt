package com.eny.i18n.extensions.lang.js.extractors

import com.eny.i18n.plugin.parser.KeyExtractor
import com.eny.i18n.plugin.parser.RawKey
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.type
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement

/**
 * Extracts i18n key from js string literal
 */
class StringLiteralKeyExtractor: KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean = listOf("JS:STRING_LITERAL", "String").any{element.type().contains(it)}

    override fun extract(element: PsiElement): RawKey = RawKey(listOf(KeyElement.literal(element.text.unQuote())))
}
