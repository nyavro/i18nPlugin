package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.ExpressionKeyParser
import com.eny.i18n.plugin.utils.FullKey
import com.intellij.psi.PsiElement

interface KeyExtractor {
    fun canExtract(element: PsiElement): Boolean
    fun extract(element: PsiElement, parser: ExpressionKeyParser, settings: Settings): FullKey?
}