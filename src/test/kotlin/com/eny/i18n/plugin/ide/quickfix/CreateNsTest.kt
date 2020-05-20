package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class CreateTranslationFileQuickFixBase(private val ext: String): BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/resources/quickfix"

    fun testTsxCreateTranslation() = myFixture.runWithConfig(
            Config(yamlContentGenerationEnabled = ext == "yml", jsonContentGenerationEnabled = ext == "json")) {
        myFixture.configureByFiles("tsx/unknownNs.tsx")
        val action = myFixture.getAllQuickFixes().find {it.text.endsWith("translation files")}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("main.$ext", "assets/mainExpected.$ext", true)
    }
}

internal class CreateJsonTranslationTest: CreateTranslationFileQuickFixBase("json")
internal class CreateYamlTranslationTest: CreateTranslationFileQuickFixBase("yml")