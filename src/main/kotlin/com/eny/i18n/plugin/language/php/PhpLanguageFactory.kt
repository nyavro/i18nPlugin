package com.eny.i18n.plugin.language.php

import com.eny.i18n.plugin.factory.*
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.extensions.lang.js.extractors.StringLiteralKeyExtractor
import com.eny.i18n.plugin.utils.type
import com.eny.i18n.plugin.utils.default
import com.eny.i18n.plugin.utils.unQuote
import com.eny.i18n.plugin.utils.whenMatches
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.FunctionReference
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression

/**
 * Php language components factory
 */
class PhpLanguageFactory: LanguageFactory {
    override fun translationExtractor(): TranslationExtractor = PhpTranslationExtractor()
    override fun referenceAssistant(): ReferenceAssistant = PhpReferenceAssistant()
}

internal class PhpTranslationExtractor: TranslationExtractor {
    override fun canExtract(element: PsiElement): Boolean =
        (element.isPhpStringLiteral() || element.isBorderToken())
    override fun isExtracted(element: PsiElement): Boolean =
        PhpPatternsExt.phpArgument("t", 0).accepts(getTextElement(element.parent))
    override fun template(element: PsiElement): (argument: String) -> String = {"t($it)"}
    override fun text(element: PsiElement): String = getTextElement(element).text.unQuote()
    override fun textRange(element: PsiElement): TextRange = getTextElement(element).parent.textRange
    private fun getTextElement(element: PsiElement) =
        element.whenMatches {it.isBorderToken()}?.prevSibling.default(element)
    private fun PsiElement.isBorderToken(): Boolean = listOf("right double quote", "right single quote").contains(this.type())
    private fun PsiElement.isPhpStringLiteral(): Boolean = listOf("double quoted string", "single quoted string").contains(this.type())
}

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