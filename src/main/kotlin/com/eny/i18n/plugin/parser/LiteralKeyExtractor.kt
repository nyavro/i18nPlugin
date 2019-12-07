package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.ExpressionKeyParser
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.KeyElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralValue
import com.intellij.psi.xml.XmlElementType

class LiteralKeyExtractor(): KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean =
        element is PsiLiteralValue && element.node.elementType != XmlElementType.XML_ATTRIBUTE_VALUE

    override fun extract(element: PsiElement, parser: ExpressionKeyParser, settings: Settings): FullKey? {
        val value: Any? = (element as PsiLiteralValue).value
        return if (value is String)
            parser.parse(listOf(KeyElement.literal(value)), false, settings.nsSeparator, settings.keySeparator, settings.stopCharacters)
        else
            null
    }
}