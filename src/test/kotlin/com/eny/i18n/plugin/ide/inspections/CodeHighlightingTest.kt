package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import utils.randomOf

abstract class CodeHighlightingTestBase(private val codeGenerator: CodeGenerator, private val translationGenerator: TranslationGenerator): BasePlatformTestCase() {

    private val testConfig = Config(vueDirectory = "assets", defaultNs = "translation")

    private fun check(fileName: String, code: String, translationName: String, translation: String) = myFixture.runWithConfig(testConfig) {
        myFixture.addFileToProject(translationName, translation)
        myFixture.configureByText(fileName, code)
        myFixture.checkHighlighting(true, true, true, true)
    }

    fun testUnresolvedNs() = check(
        "unresolvdNs.${codeGenerator.ext()}",
        codeGenerator.multiGenerate(
            "\"<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base\"",
            "`<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base.\${arg}`"
        ),
        "test.${translationGenerator.ext()}",
        translationGenerator.generateContent("root", "first", "key", "value")
    )

    fun testUnresolvedKey() = check(
        "unresolvedKey.${codeGenerator.ext()}",
        codeGenerator.multiGenerate(
            "\"test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key</warning>\"",
            "\"test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\"",
            "`test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key.\${arg}</warning>`",
            "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${arg}</warning>`",
            "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${arg}</warning>`",
            "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${b ? 'key' : 'key2'}</warning>`",
            "`test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.\${b ? 'key' : 'key2'}</warning>`"
        ),
        "test.${translationGenerator.ext()}",
        translationGenerator.generateContent("tst1", "base", "single", "only one value")
    )

    fun testReferenceToObject() = check(
        "refToObject.${codeGenerator.ext()}",
        codeGenerator.generate("\"test:<warning descr=\"Reference to object\">tst2.plurals</warning>\""),
        "test.${translationGenerator.ext()}",
        translationGenerator.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )
}

class CodeHighlightingRandomTest: CodeHighlightingTestBase(
    randomOf(
        ::JsCodeGenerator,
        ::TsCodeGenerator,
        ::JsxCodeGenerator,
        ::TsxCodeGenerator)(),
    randomOf(
        ::JsonTranslationGenerator,
        ::YamlTranslationGenerator)()
)
//class PhpJsonCodeHighlightingTest(): CodeHighlightingBase(PhpCodeGenerator(), JsonTranslationGenerator())

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
        check("php/unresolvedNs.php")
    }

    fun testUnresolvedKey() = myFixture.runWithConfig(testConfig) {
        check("php/unresolvedKey.php", translation)
    }

    fun testReferenceToObject() = myFixture.runWithConfig(testConfig) {
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