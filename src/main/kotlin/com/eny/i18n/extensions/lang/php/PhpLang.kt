package com.eny.i18n.extensions.lang.php

import com.eny.i18n.Lang
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.plugin.language.php.PhpPatternsExt
import com.eny.i18n.plugin.parser.LiteralKeyExtractor
import com.eny.i18n.plugin.parser.StringLiteralKeyExtractor
import com.eny.i18n.plugin.parser.TemplateKeyExtractor
import com.eny.i18n.plugin.parser.type
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class PhpLang: Lang {
    override fun canExtractKey(element: PsiElement): Boolean {
        return listOf("String").contains(element.type()) &&
                PlatformPatterns.or(
                    PhpPatternsExt.phpArgument("t", 0),
                    gettextPattern(Settings.getInstance(element.project).config())
                ).let { pattern ->
                    pattern.accepts(element) ||
                            pattern.accepts(PsiTreeUtil.findFirstParent(element, { it.parent?.type() == "Parameter list" }))
                }
    }

    override fun extractFullKey(element: PsiElement): FullKey? {
        val config = Settings.getInstance(element.project).config()
        val parser = (
                if (config.gettext)
                    KeyParserBuilder.withoutTokenizer()
                else
                    KeyParserBuilder
                            .withSeparators(config.nsSeparator, config.keySeparator)
                            .withDummyNormalizer()
                            .withTemplateNormalizer()
                ).build()
        return listOf(
                TemplateKeyExtractor(),
                LiteralKeyExtractor(),
                StringLiteralKeyExtractor(),
        )
                .find {it.canExtract(element)}
                ?.let{parser.parse(it.extract(element), config.gettext, config.firstComponentNs)}
    }

    private fun gettextPattern(config: Config) =
        PlatformPatterns.or(*config.gettextAliases.split(",").map { PhpPatternsExt.phpArgument(it.trim(), 0) }.toTypedArray())
}
