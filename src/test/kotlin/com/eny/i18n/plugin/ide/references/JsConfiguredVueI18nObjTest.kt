package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class JsConfiguredVueI18nObjTest : BasePlatformTestCase() {

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
//
//    fun testInvalidConfiguration() {
//        myFixture.configureByFiles(
//            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid/i18nConfig.js")
//        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//        assertNotNull(element)
//        assertNull(element!!.references[0].resolve()?.text)
//    }
//
//    fun testInvalidConfigurationObject() {
//        myFixture.configureByFiles(
//            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid/i18n.js")
//        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//        assertNotNull(element)
//        assertNull(element!!.references[0].resolve()?.text)
//    }
//
//    fun testInvalidConfigurationMissingMessages() {
//        myFixture.configureByFiles(
//            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid2/i18n.js")
//        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//        assertNotNull(element)
//        assertNull(element!!.references[0].resolve()?.text)
//    }
//
//    fun testInvalidConfigurationMessages() {
//        myFixture.configureByFiles(
//            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid3/i18n.js")
//        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//        assertNotNull(element)
//        assertNull(element!!.references[0].resolve()?.text)
//    }

//    fun testReferencesChainIsTooLong() {
//        myFixture.configureByFiles(
//            "vue/refVue-i18n-js-conf1.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid4/i18n.js")
//        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//        assertNotNull(element)
//        assertEquals("message", element!!.references[0].resolve()?.text)
//    }
//
//    fun testReference() {
//        myFixture.configureByFiles(
//            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/i18n.js")
//        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//        assertNotNull(element)
//        assertEquals("'hello!!'", element!!.references[0].resolve()?.text)
//    }

//    fun testReference1() {
//        myFixture.configureByFiles(
//            "vue/refVue-i18n-js-conf1.vue", "jsConfigured/vue-i18n/vue-i18n-object/valid1/i18n.js")
//        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//        assertNotNull(element)
//        assertEquals("'hey there!!!'", element!!.references[0].resolve()?.text)
//    }

//    fun testReference2() {
//        myFixture.configureByFiles(
//            "vue/refVue-i18n-js-conf1.vue", "jsConfigured/vue-i18n/vue-i18n-object/valid2/i18n.js")
//        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//        assertNotNull(element)
//        assertEquals("'hey there!!!'", element!!.references[0].resolve()?.text)
//    }
}
