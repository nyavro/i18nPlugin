package com.eny.i18n.plugin.ide.annotator

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