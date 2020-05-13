package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.runVue

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

    fun testKeyContext() = myFixture.runVue {
        doUnavailable("vue/keyContext.vue")
    }

    fun testKeyContextScript() = myFixture.runVue {
        myFixture.tempDirPath
        doUnavailable("vue/keyContextScript.vue")
    }
}