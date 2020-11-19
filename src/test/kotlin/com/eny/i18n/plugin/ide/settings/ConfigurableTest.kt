package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.utils.PluginBundle
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ConfigurableTest {

    @Test
    fun testConfigurable() {
        val configurable = Configurable(mockk())
        assertEquals(PluginBundle.getMessage("app.name"), configurable.displayName)
        assertEquals("preference.i18nPlugin", configurable.helpTopic)
        assertEquals("preference.i18nPlugin", configurable.id)
        assertNotNull(configurable.createComponent())
    }
}