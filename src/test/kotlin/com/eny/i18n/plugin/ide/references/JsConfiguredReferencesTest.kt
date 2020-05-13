package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class JsConfiguredReferences : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/references"
    }

    override fun setUp() {
        super.setUp()
        Settings.getInstance(myFixture.project).let {
            it.vue = true
            it.vueDirectory = "i18n"
        }
    }

    override fun tearDown() {
        Settings.getInstance(myFixture.project).let {
            it.vue = false
            it.vueDirectory = "locales"
        }
        super.tearDown()
    }

//    fun testReference() {
//        myFixture.configureByFiles(
//            "vue/testReference.vue", "jsConfigured/i18n/index.js", "jsConfigured/i18n/en-uk/index.js", "jsConfigured/i18n/en-uk/profile.js")
//        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//        assertNotNull(element)
//        assertEquals("'Form title'", element!!.references[0].resolve()?.text)
//    }
}
