package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.ide.PluginBundle
import org.junit.Test

class CommonSettingsPanelTest: SettingsPanelBase<CommonSettings>(::CommonSettingsFormFragment, ::CommonSettings) {

    @Test
    fun testSearchInProjectFilesOnly() {
        checkBooleanProperty(PluginBundle.getMessage("settings.search.in.project.files.only"), CommonSettings::searchInProjectOnly)
    }

    @Test
    fun testFoldingEnabled() {
        checkBooleanProperty(PluginBundle.getMessage("settings.folding.enabled"), CommonSettings::foldingEnabled)
    }

    @Test
    fun testExtractSorted() {
        checkBooleanProperty(PluginBundle.getMessage("settings.extraction.sorted"), CommonSettings::extractSorted)
    }

    @Test
    fun testPartiallyTraslated() {
        checkBooleanProperty(PluginBundle.getMessage("settings.annotations.partially.translated.enabled"), CommonSettings::partialTranslationInspectionEnabled)
    }

    @Test
    fun testFoldingPreferredLanguage() {
        checkStringProperty("jp", PluginBundle.getMessage("settings.folding.preferredLanguage"), CommonSettings::foldingPreferredLanguage)
    }
}