package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.annotator.type
import com.eny.i18n.plugin.key.KeyElement
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement

/**
 * Extracts i18n key from js string literal
 */
class StringLiteralKeyExtractor: KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean =
        listOf("JS:STRING_LITERAL", "quoted string", "String").any{element.type().contains(it)}

    override fun extract(element: PsiElement): Pair<List<KeyElement>, List<String>?> =
        Pair(listOf(KeyElement.literal(element.text.unQuote())), null)
}