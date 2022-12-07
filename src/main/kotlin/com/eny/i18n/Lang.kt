package com.eny.i18n

import com.eny.i18n.plugin.parser.RawKey
import com.intellij.psi.PsiElement

interface Lang {

    fun canExtractKey(element: PsiElement): Boolean

    fun extractRawKey(element: PsiElement): RawKey?
}