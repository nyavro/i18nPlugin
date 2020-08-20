package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.nullableToList
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

/**
 * Extracts i18n key from js string literal
 */
class XmlAttributeKeyExtractor: KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean = (element is XmlAttribute) && element.name == "i18nKey"

    override fun extract(element: PsiElement): Pair<List<KeyElement>, List<String>?> =
        Pair(
            (element as? XmlAttribute)
                ?.value
                .nullableToList()
                .map {KeyElement.literal(it)},
            null
        )
}