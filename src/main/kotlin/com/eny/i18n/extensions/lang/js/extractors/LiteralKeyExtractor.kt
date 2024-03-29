package com.eny.i18n.extensions.lang.js.extractors

import com.eny.i18n.plugin.parser.KeyExtractor
import com.eny.i18n.plugin.parser.RawKey
import com.eny.i18n.plugin.utils.KeyElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralValue
import com.intellij.psi.xml.XmlElementType
/**
 * Extracts translation key from psi literal
 */
class LiteralKeyExtractor: KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean =
        element is PsiLiteralValue && element.node.elementType != XmlElementType.XML_ATTRIBUTE_VALUE

    override fun extract(element: PsiElement): RawKey =
        RawKey(
            (element as PsiLiteralValue).value?.let {it as? String}?.let {
                listOf(KeyElement.literal(it))
            } ?: listOf(),
            listOf()
        )
}

