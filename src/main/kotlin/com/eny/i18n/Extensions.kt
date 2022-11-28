package com.eny.i18n

import com.intellij.openapi.extensions.ExtensionPointName

class Extensions {
    companion object {
        val LOCALIZATION_SOURCE_PROVIDER = ExtensionPointName.create<LocalizationSourceProvider>("com.eny.i18n.localizationSourceProvider")
        val LOCALIZATION = ExtensionPointName.create<Localization>("com.eny.i18n.localization")
    }
}