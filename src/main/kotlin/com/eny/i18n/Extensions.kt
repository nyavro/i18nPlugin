package com.eny.i18n

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.psi.PsiElement

class Extensions {
    companion object {
        val LOCALIZATION = ExtensionPointName.create<Localization<PsiElement>>("com.eny.i18n.localization")
        val LANG = ExtensionPointName.create<Lang>("com.eny.i18n.lang")
        val TECHNOLOGY = ExtensionPointName.create<Technology>("com.eny.i18n.technology")
    }
}
