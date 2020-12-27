package com.eny.i18n.plugin.extension

import com.eny.i18n.plugin.factory.ContentGenerator
import com.eny.i18n.plugin.factory.CustomSettings
import com.eny.i18n.plugin.factory.LocalizationSourcesProvider
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.extensions.ProjectExtensionPointName

class Extensions {

    companion object {
        val CONTENT_GENERATORS: ExtensionPointName<ContentGenerator> = ExtensionPointName.create("com.eny.i18n.contentGenerator")
        val LOCALIZATION_SOURCE_PROVIDERS: ExtensionPointName<LocalizationSourcesProvider> = ExtensionPointName.create("com.eny.i18n.localizationSourceProvider")
        val SETTINGS = ProjectExtensionPointName<CustomSettings>("com.eny.i18n.customSettings")
    }
}