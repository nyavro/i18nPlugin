package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.utils.PluginBundle
import io.mockk.mockk
import io.mockk.unmockkAll
import net.sourceforge.marathon.javadriver.JavaDriver
import net.sourceforge.marathon.javadriver.JavaProfile
import org.junit.After
import org.junit.Before
import org.junit.Test
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.reflect.KMutableProperty1
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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