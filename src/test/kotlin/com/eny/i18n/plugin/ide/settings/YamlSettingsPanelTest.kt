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

class YamlSettingsPanelTest: SettingsPanelBase<YamlSettings>(::YamlSettingsFormFragment, ::YamlSettings) {

    @Test
    fun testPreferYamlFilesGeneration() {
        checkBooleanProperty(PluginBundle.getMessage("settings.prefer.yaml.files.generation"), YamlSettings::preferYamlFilesGeneration)
    }
}