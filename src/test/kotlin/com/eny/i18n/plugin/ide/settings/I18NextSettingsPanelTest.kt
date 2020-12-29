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