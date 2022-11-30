package com.eny.i18n

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.psi.PsiElement

class Extensions {
    companion object {
        val LOCALIZATION_SOURCE_PROVIDER = ExtensionPointName.create<LocalizationSourceProvider>("com.eny.i18n.localizationSourceProvider")
        val LOCALIZATION = ExtensionPointName.create<Localization<PsiElement>>("com.eny.i18n.localization")
    }
}