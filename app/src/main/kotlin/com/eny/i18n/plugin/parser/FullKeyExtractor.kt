package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.factory.CallContext
import com.eny.i18n.plugin.ide.settings.i18NextSettings
import com.eny.i18n.plugin.ide.settings.poSettings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.intellij.psi.PsiElement

/**
 * Gets element's type string
 */
fun PsiElement.type(): String = this.node?.elementType.toString()

interface Extractor {
    fun extractFullKey(element: PsiElement): FullKey?
}

class DummyContext: CallContext {
    override fun accepts(element: PsiElement): Boolean = true
}

/**
 * Extracts translation key from psi element
 */
class KeyExtractorImpl: Extractor {

    /**
     * Extracts fullkey from element, if possible
     */
    override fun extractFullKey(element: PsiElement): FullKey? {
        val config = element.project.i18NextSettings()
        val gettext = false //element.project.poSettings().gettext
        val parser = (
            if (gettext)
                KeyParserBuilder.withoutTokenizer()
            else
                KeyParserBuilder
                    .withSeparators(config.nsSeparator, config.keySeparator)
                    .withNormalizer(DummyTextNormalizer())
                    .withNormalizer(ExpressionNormalizer())
        ).build()
        return listOf(
            ReactUseTranslationHookExtractor(),
            TemplateKeyExtractor(),
            LiteralKeyExtractor(),
            StringLiteralKeyExtractor(),
            XmlAttributeKeyExtractor()
        )
            .find {it.canExtract(element)}
            ?.let{parser.parse(it.extract(element))}
    }
}