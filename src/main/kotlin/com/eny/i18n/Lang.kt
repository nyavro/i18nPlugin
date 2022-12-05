package com.eny.i18n

import com.eny.i18n.plugin.factory.CallContext
import com.eny.i18n.plugin.key.FullKey
import com.intellij.psi.PsiElement

interface Lang {

    fun canExtractKey(element: PsiElement): Boolean

    fun extractFullKey(element: PsiElement): FullKey?
}