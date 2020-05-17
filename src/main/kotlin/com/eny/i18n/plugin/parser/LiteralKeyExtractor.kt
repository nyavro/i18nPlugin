package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralValue
import com.intellij.psi.xml.XmlElementType
/**
 * Extracts translation key from psi literal
 */
class LiteralKeyExtractor: KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean {
        val b = element is PsiLiteralValue && element.node.elementType != XmlElementType.XML_ATTRIBUTE_VALUE
        return b
    }

    override fun extract(element: PsiElement): List<KeyElement> =
        (element as PsiLiteralValue).value?.let {it as? String}?.let {
            listOf(KeyElement.literal(it))
        } ?: listOf()

}

