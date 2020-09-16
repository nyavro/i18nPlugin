package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import org.junit.jupiter.api.Test

abstract class JsConfiguredReferencesTest : PlatformBaseTest() {

    override fun getTestDataPath(): String {
        return "src/test/resources/references"
    }

    @Test
    fun testReference() = myFixture.runVueConfig(Config(vueDirectory = "i18n")) {
        myFixture.configureByFiles(
            "vue/testReference.vue", "jsConfigured/i18n/index.js", "jsConfigured/i18n/en-uk/index.js", "jsConfigured/i18n/en-uk/profile.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("'Form title'", element!!.references[0].resolve()?.text)
    }
}
