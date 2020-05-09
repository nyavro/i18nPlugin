package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.FullKey
import com.intellij.psi.PsiElement

/**
 * Extracts key from js string template
 */
class TemplatePartKeyExtractor: KeyExtractor {
    private val keyExtractor = FullKeyExtractor(DummyContext(), KeyExtractorImpl())

    override fun canExtract(element: PsiElement): Boolean = element.type() == "JS:STRING_TEMPLATE_PART"

    override fun extract(element: PsiElement, parser: ExpressionKeyParser, settings: Settings): FullKey? =
        keyExtractor.extractI18nKeyLiteral(element.parent)
}