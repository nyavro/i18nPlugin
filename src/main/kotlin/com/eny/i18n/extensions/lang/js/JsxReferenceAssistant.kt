package com.eny.i18n.extensions.lang.js

import com.eny.i18n.extensions.lang.js.extractors.XmlAttributeKeyExtractor
import com.eny.i18n.plugin.factory.ReferenceAssistant
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement

class JsxReferenceAssistant: ReferenceAssistant {

    override fun pattern(): ElementPattern<out PsiElement> {
        return XmlPatterns.xmlAttributeValue("i18nKey")
    }
    override fun extractKey(element: PsiElement): FullKey? {
        val config = Settings.getInstance(element.project).config()
        val parser = KeyParserBuilder.withSeparators(config.nsSeparator, config.keySeparator)
            .withTemplateNormalizer()
            .build()
        return listOf(
                XmlAttributeKeyExtractor()
        )
            .find {it.canExtract(element)}
            ?.let {parser.parse(it.extract(element))}
    }
}