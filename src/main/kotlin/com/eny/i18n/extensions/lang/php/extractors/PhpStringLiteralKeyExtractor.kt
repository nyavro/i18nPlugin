package com.eny.i18n.extensions.lang.php.extractors

import com.eny.i18n.plugin.parser.KeyExtractor
import com.eny.i18n.plugin.parser.RawKey
import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement

/**
 * Extracts i18n key from js string literal
 */
class PhpStringLiteralKeyExtractor: KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean =
        listOf("quoted string").any{element.type().contains(it)}

    override fun extract(element: PsiElement): RawKey =
            RawKey(listOf(KeyElement.literal(element.text.unQuote())))
}