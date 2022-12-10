package com.eny.i18n.extensions.lang.php

import com.eny.i18n.extensions.lang.js.extractors.StringLiteralKeyExtractor
import com.eny.i18n.plugin.factory.ReferenceAssistant
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.plugin.language.php.PhpPatternsExt
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement

internal class PhpReferenceAssistant: ReferenceAssistant {

    override fun pattern(): ElementPattern<out PsiElement> {
        return PhpPatternsExt.phpArgument()
    }

    override fun extractKey(element: PsiElement): FullKey? {
        val config = Settings.getInstance(element.project).config()
        if (config.gettext) {
            if (!gettextPattern(Settings.getInstance(element.project).config()).accepts(element)) return null
        }
        val parser = (
            if (config.gettext) {
                KeyParserBuilder.withoutTokenizer()
            } else
                KeyParserBuilder.withSeparators(config.nsSeparator, config.keySeparator)
        ).build()
        return listOf(StringLiteralKeyExtractor())
            .find { it.canExtract(element) }
            ?.let { parser.parse(it.extract(element)) }
    }

    private fun gettextPattern(config: Config) =
        PlatformPatterns.or(*config.gettextAliases.split(",").map { PhpPatternsExt.phpArgument(it.trim(), 0) }.toTypedArray())
}