package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.ExpressionKeyParser
import com.eny.i18n.plugin.utils.FullKey
import com.intellij.psi.PsiElement

class TemplatePartKeyExtractor(): KeyExtractor {
    private val keyExtractor = FullKeyExtractor()

    override fun canExtract(element: PsiElement): Boolean = element.type() == "JS:STRING_TEMPLATE_PART"

    override fun extract(element: PsiElement, parser: ExpressionKeyParser, normalizer: KeyNormalizer, settings: Settings): FullKey? =
        keyExtractor.extractI18nKeyLiteral(element.parent)
}