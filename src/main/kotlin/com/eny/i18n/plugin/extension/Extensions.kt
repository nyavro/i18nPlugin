package com.eny.i18n.plugin.extension

import com.eny.i18n.plugin.factory.CustomSettings
import com.eny.i18n.plugin.factory.LanguageFactory
import com.eny.i18n.plugin.factory.LocalizationFactory
import com.eny.i18n.plugin.factory.LocalizationSourcesProvider
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.extensions.ProjectExtensionPointName

class Extensions {

    companion object {
        val LOCALIZATION_FACTORIES: ExtensionPointName<LocalizationFactory> = ExtensionPointName.create("com.eny.i18n.localizationFactory")
        val TECHNOLOGY_FACTORIES: ExtensionPointName<LanguageFactory> = ExtensionPointName.create("com.eny.i18n.technologyFactory")
        val LOCALIZATION_SOURCE_PROVIDERS: ExtensionPointName<LocalizationSourcesProvider> = ExtensionPointName.create("com.eny.i18n.localizationSourceProvider")
        val SETTINGS = ProjectExtensionPointName<CustomSettings>("com.eny.i18n.customSettings")
    }
}