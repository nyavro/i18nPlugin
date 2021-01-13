package com.eny.i18n.plugin.yaml

import com.eny.i18n.plugin.ide.settings.SettingsPanelBase
import com.eny.i18n.plugin.utils.PluginBundle
import org.junit.Test

class YamlSettingsPanelTest: SettingsPanelBase<YamlSettings>(::YamlSettingsFormFragment, ::YamlSettings) {

    @Test
    fun testPreferYamlFilesGeneration() {
        checkBooleanProperty(PluginBundle.getMessage("settings.prefer.yaml.files.generation"), YamlSettings::preferYamlFilesGeneration)
    }
}