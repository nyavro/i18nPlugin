package com.eny.i18n.extensions.lang.php

import com.eny.i18n.Lang
import com.eny.i18n.extensions.lang.php.extractors.PhpStringLiteralKeyExtractor
import com.eny.i18n.plugin.parser.*
import com.intellij.psi.PsiElement

class PhpLang: Lang {
    override fun canExtractKey(element: PsiElement): Boolean {
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
        return listOf(
//            TemplateKeyExtractor(),
//            LiteralKeyExtractor(),
            PhpStringLiteralKeyExtractor()
        ).find {it.canExtract(element)}?.extract(element)
    }
}

