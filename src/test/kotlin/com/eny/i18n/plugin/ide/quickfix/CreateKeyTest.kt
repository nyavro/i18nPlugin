package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.JsCodeAndTranslationGenerators
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class CreateKeyTest: PlatformBaseTest() {

    @ParameterizedTest
    @ArgumentsSource(JsCodeAndTranslationGenerators::class)
    fun testCreateKey(cg: CodeGenerator, tg: TranslationGenerator) {
        val hint = "Create i18n key"
        myFixture.addFileToProject("assets/test.${tg.ext()}", tg.generateContent("ref", "section", "key", "Translation"))
        myFixture.configureByText("simple.${cg.ext()}", cg.generate("\"test:ref.section.mi<caret>ssing\""))
        val action = myFixture.filterAvailableIntentions(hint).find {it.text == hint}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResult(
            "assets/test.${tg.ext()}",
            tg.generateNamedBlock(
                "ref",
                tg.generateNamedBlock("section",
                    tg.generate(
                        arrayOf("key", "Translation"),
                        arrayOf("missing", "test:ref.section.missing")
                    ),
                    1
                )
            ),
            false
        )
    }
}

internal abstract class CreateKeyTestBase(private val base: String): PlatformBaseTest() {
    override fun getTestDataPath(): String = "src/test/resources/quickfix"

    private val testConfig = Config(jsonContentGenerationEnabled = base == "json", yamlContentGenerationEnabled = base == "yml")

    @Test
    fun testTsxCreateKey() = myFixture.runWithConfig(testConfig) {
        val hint = "Create i18n key"
        myFixture.configureByFiles("tsx/simple.tsx", "assets/test.$base")
        val action = myFixture.filterAvailableIntentions(hint).find {ac -> ac.text == hint}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("assets/test.$base", "assets/testExpected.$base", false)
    }

    @Test
    fun testTsxCreateKeyMultipleTranslations() = myFixture.runWithConfig(testConfig) {
        myFixture.configureByFiles("tsx/simple.tsx", "assets/ru/test.$base", "assets/en/test.$base")
        val action = myFixture.findSingleIntention("Create i18n key in all translation files")
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("assets/en/test.$base", "assets/testExpected.$base", false)
    }
}

internal class CreateKeyInJsonTest: CreateKeyTestBase("json")

internal class CreateKeyInYamlTest: CreateKeyTestBase("yml")