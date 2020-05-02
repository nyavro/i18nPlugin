package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class JsConfiguredVueI18nObjTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/references/"
    }

    override fun setUp() {
        super.setUp()
        Settings.getInstance(myFixture.project).let {
            it.vue = true
            it.jsConfiguration = "i18n.js"
        }
    }

    override fun tearDown() {
        Settings.getInstance(myFixture.project).let {
            it.vue = false
            it.jsConfiguration = ""
        }
        super.tearDown()
    }

    fun testInvalidConfiguration() {
        myFixture.configureByFiles(
            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid/i18nConfig.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertNull(element!!.references[0].resolve()?.text)
    }

    fun testInvalidConfigurationObject() {
        myFixture.configureByFiles(
            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid/i18n.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertNull(element!!.references[0].resolve()?.text)
    }

    fun testReference() {
        myFixture.configureByFiles(
            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/i18n.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("'hello!!'", element!!.references[0].resolve()?.text)
    }
}
