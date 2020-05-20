package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.factory.CallContext
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParser
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

    private val parser: KeyParser = KeyParser()
    /**
     * Converts element to it's literal value, if possible
     */
    override fun extractFullKey(element: PsiElement): FullKey? {
        val config = Settings.getInstance(element.project).config()
        return listOf(
            TemplateKeyExtractor(),
            LiteralKeyExtractor(),
            StringLiteralKeyExtractor()
        )
            .find {it.canExtract(element)}
            ?.let{parser.parse(it.extract(element), config.nsSeparator, config.keySeparator)}
    }
}