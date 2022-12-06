package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.nullableToList
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue

/**
 * Extracts i18n key from js string literal
 */
class XmlAttributeKeyExtractor: KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean = XmlPatterns.xmlAttributeValue("i18nKey").accepts(element)

    override fun extract(element: PsiElement): RawKey =
        RawKey(
            (element as? XmlAttributeValue)
                ?.value
                .nullableToList()
                .map {KeyElement.literal(it)}
        )
}