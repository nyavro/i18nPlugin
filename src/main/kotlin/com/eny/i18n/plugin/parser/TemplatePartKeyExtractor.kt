package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.FullKeyExtractor
import com.eny.i18n.plugin.key.parser.KeyParser
import com.intellij.psi.PsiElement

/**
 * Extracts key from js string template
 */
class TemplatePartKeyExtractor: KeyExtractor {
    private val keyExtractor = FullKeyExtractor(DummyContext(), KeyExtractorImpl())

    override fun canExtract(element: PsiElement): Boolean = element.type() == "JS:STRING_TEMPLATE_PART"

    override fun extract(element: PsiElement, parser: KeyParser, settings: Settings): FullKey? =
        keyExtractor.extractI18nKeyLiteral(element.parent)
}