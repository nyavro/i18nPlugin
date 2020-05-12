package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.settings.Settings

abstract class KeyContextBase(private val language: String): ExtractionTestBase() {

    fun testKeyContext() {
        doUnavailable("$language/keyContext.$language")
    }

    fun testKeyContextDefaultNs() {
        doUnavailable("$language/keyContextDefNs.$language")
    }
}

class KeyContextJsTest: KeyContextBase("js")
class KeyContextTsTest: KeyContextBase("ts")
class KeyContextJsxTest: KeyContextBase("tsx")
class KeyContextTsxTest: KeyContextBase("jsx")
class KeyContextPhpTest: KeyContextBase("php")

class KeyContextVueTest: ExtractionTestBase() {

    fun testKeyContext() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        doUnavailable("vue/keyContext.vue")
        settings.vue = false
    }

    fun testKeyContextScript() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        doUnavailable("vue/keyContextScript.vue")
        settings.vue = false
    }
}