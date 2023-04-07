package com.eny.i18n.extensions.lang.php

import com.eny.i18n.Lang
import com.eny.i18n.TranslationFunction
import com.eny.i18n.extensions.lang.php.extractors.PhpStringLiteralKeyExtractor
import com.eny.i18n.plugin.factory.FoldingProvider
import com.eny.i18n.plugin.factory.TranslationExtractor
import com.eny.i18n.plugin.parser.RawKey
import com.intellij.psi.PsiElement

class PhpLang: Lang {
    override fun canExtractKey(element: PsiElement, translationFunctions: List<TranslationFunction>): Boolean {
        return extractRawKey(element) != null
//        listOf("String").contains(element.type()) &&
//                PlatformPatterns.or(
//                    PhpPatternsExt.phpArgument("t", 0),
//                    gettextPattern(Settings.getInstance(element.project).config())
//                ).let { pattern ->
//                    pattern.accepts(element) ||
//                            pattern.accepts(PsiTreeUtil.findFirstParent(element, { it.parent?.type() == "Parameter list" }))
//                }
    }

    override fun extractRawKey(element: PsiElement): RawKey? {
        val extractor = PhpStringLiteralKeyExtractor()
        return if (extractor.canExtract(element)) extractor.extract(element) else null
    }

    override fun foldingProvider(): FoldingProvider = PhpFoldingProvider()

    override fun translationExtractor(): TranslationExtractor = PhpTranslationExtractor()

    override fun resolveLiteral(entry: PsiElement): PsiElement? {
        val typeName = entry.node.elementType.toString()
        return if (typeName == "single quoted string") entry else null
    }
}

