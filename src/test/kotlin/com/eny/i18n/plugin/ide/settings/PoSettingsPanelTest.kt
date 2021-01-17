package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.addons.technology.po.PlainObjectSettingsFormFragment
import com.eny.i18n.plugin.addons.technology.po.PoSettings
import com.eny.i18n.plugin.utils.PluginBundle
import org.junit.Test

class PoSettingsPanelTest: SettingsPanelBase<PoSettings>(::PlainObjectSettingsFormFragment, ::PoSettings) {

    @Test
    fun testGettext() {
        checkBooleanProperty(PluginBundle.getMessage("settings.gettext.enabled"), PoSettings::gettext)
    }

    @Test
    fun testGettextAliases() {
        checkStringProperty("alias1,alias2", PluginBundle.getMessage("settings.gettext.aliases"), PoSettings::gettextAliases)
    }
}