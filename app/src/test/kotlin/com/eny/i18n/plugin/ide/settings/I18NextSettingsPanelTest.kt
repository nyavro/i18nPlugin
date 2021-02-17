package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.addons.technology.i18n.I18NextSettings
import com.eny.i18n.plugin.addons.technology.i18n.I18NextSettingsFormFragment
import com.eny.i18n.plugin.utils.PluginBundle
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class I18NextSettingsPanelTest: SettingsPanelBase<I18NextSettings>(::I18NextSettingsFormFragment, ::I18NextSettings) {

    @Test
    fun testInvalidSeparator() = runWithSettings(I18NextSettings()) { settings ->
        val message = PluginBundle.getMessage("settings.key.separator")
        val cb = driver.findElementByName(message)
        assertNotNull(cb)
        val value = cb.text
        assertEquals(value, settings.keySeparator)
        cb.clear()
        cb.sendKeys(" {}\$")
        assertEquals("", settings.keySeparator)
    }

    @Test
    fun testNsSeparator() {
        checkStringProperty("#", PluginBundle.getMessage("settings.namespace.separator"), I18NextSettings::nsSeparator)
    }

    @Test
    fun testKeySeparator() {
        checkStringProperty("@", PluginBundle.getMessage("settings.key.separator"), I18NextSettings::keySeparator)
    }

    @Test
    fun testPluralSeparator() {
        checkStringProperty("%", PluginBundle.getMessage("settings.plural.separator"), I18NextSettings::pluralSeparator)
    }

    @Test
    fun testDefaultNs() {
        checkStringProperty("testloc", PluginBundle.getMessage("settings.default.namespace"), I18NextSettings::defaultNs)
    }
}