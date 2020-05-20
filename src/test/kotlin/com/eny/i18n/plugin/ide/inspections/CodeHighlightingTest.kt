package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class CodeHighlightingTest : BasePlatformTestCase() {

    private val translation = "assets/test.json"

    override fun getTestDataPath(): String {
        return "src/test/resources/codeHighlighting"
    }

    private val testConfig = Config(vueDirectory = "assets", defaultNs = "translation")

    private fun check(filePath: String) = myFixture.runWithConfig(testConfig) {
        myFixture.configureByFiles(filePath)
        myFixture.checkHighlighting(true, false, true, true)
    }

    private fun check(filePath: String, assetPath: String) {
        myFixture.configureByFiles(filePath, assetPath)
        myFixture.checkHighlighting(true, true, true, true)
    }

    fun testUnresolvedNs() = myFixture.runWithConfig(testConfig) {
        check("tsx/unresolvedNs.tsx")
        check("ts/unresolvedNs.ts")
        check("jsx/unresolvedNs.jsx")
        check("js/unresolvedNs.js")
        check("php/unresolvedNs.php")
    }

    fun testUnresolvedKey() = myFixture.runWithConfig(testConfig) {
        check("tsx/unresolvedKey.tsx", translation)
        check("ts/unresolvedKey.ts", translation)
        check("jsx/unresolvedKey.jsx", translation)
        check("js/unresolvedKey.js", translation)
        check("php/unresolvedKey.php", translation)
    }

    fun testReferenceToObject() = myFixture.runWithConfig(testConfig) {
        check("tsx/refToObject.tsx", translation)
        check("ts/refToObject.ts", translation)
        check("jsx/refToObject.jsx", translation)
        check("js/refToObject.js", translation)
        check("php/refToObject.php", translation)
    }

    fun testReferenceToObjectVue() = myFixture.runVueConfig(testConfig) {
        check("vue/refToObject.vue", "assets/en-US.json")
    }

    fun testReferenceToObjectDefaultNs() = myFixture.runWithConfig(testConfig) {
        check("tsx/refToObjectDef.tsx", "assets/translation.json")
        check("ts/refToObjectDef.ts", "assets/translation.json")
        check("jsx/refToObjectDef.jsx", "assets/translation.json")
        check("js/refToObjectDef.js", "assets/translation.json")
        check("php/refToObjectDef.php", "assets/translation.json")
    }

    fun testResolved() = myFixture.runWithConfig(testConfig) {
        check("tsx/resolved.tsx", translation)
        check("ts/resolved.ts", translation)
        check("jsx/resolved.jsx", translation)
        check("js/resolved.js", translation)
        check("php/resolved.php", translation)
    }

    fun testResolvedVue() = myFixture.runVueConfig(testConfig) {
        check("vue/resolved.vue", "assets/test.json")
    }

    fun testResolvedTemplate() = myFixture.runWithConfig(testConfig) {
        check("tsx/resolvedTemplate.tsx", translation)
    }

    fun testDefaultNsUnresolved() = myFixture.runWithConfig(testConfig) {
        check("tsx/defNsUnresolved.tsx", translation)
        check("ts/defNsUnresolved.ts", translation)
        check("jsx/defNsUnresolved.jsx", translation)
        check("js/defNsUnresolved.js", translation)
        check("php/defNsUnresolved.php", translation)
    }

    fun testDefaultNsUnresolvedVue() = myFixture.runVueConfig(testConfig) {
        check("vue/unresolvedKey.vue", "assets/en-US.json")
    }

    fun testNotTranslationArgument() = myFixture.runWithConfig(testConfig) {
        check("tsx/notArg.tsx", translation)
        check("ts/notArg.ts", translation)
        check("jsx/notArg.jsx", translation)
        check("js/notArg.js", translation)
        check("php/notArg.php", translation)
    }

    fun testNotTranslationArgumentVue() = myFixture.runVueConfig(testConfig) {
        check("vue/unresolvedKey.vue", "assets/en-US.json")
    }
}