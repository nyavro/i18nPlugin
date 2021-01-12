package com.eny.i18n.plugin.ide.actions

//import com.eny.i18n.plugin.ide.JsonYamlCodeGenerators
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.translationGenerator
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.intellij.openapi.ui.Messages
import org.junit.Assert
import org.junit.Test
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.ArgumentsSource
//import org.junit.jupiter.params.provider.ValueSource

class TranslationFileGenerationTest: ExtractionTestBase() {

    // @TODO 16

    @Test
    fun testStub1() {
        assertTrue(true)
    }


//    @ParameterizedTest
//    @ArgumentsSource(JsonYamlCodeGenerators::class)
//    fun testTranslationFileGeneration(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext())) {
//        myFixture.configureByText("simple.${cg.ext()}", cg.generateBlock("<caret>I want to move it to translation"))
//        val action = myFixture.findSingleIntention(hint)
//        assertNotNull(action)
//        Messages.setTestInputDialog(predefinedTextInputDialog("main:component.header.title"))
//        myFixture.launchAction(action)
//        myFixture.checkResult(cg.generate("'main:component.header.title'"))
//        myFixture.checkResult(
//            "main.${tg.ext()}",
//            tg.generateContent("component", "header", "title", "I want to move it to translation"),
//            false
//        )
//    }
}

class VueTranslationGenerationTest: ExtractionTestBase() {

    @Test
    fun testStub1() {
        Assert.assertTrue(true)
    }
//    @ParameterizedTest
//    @ValueSource(strings = ["yml", "json"])
//    fun testTranslationFileGenerationVue(ext: String) = myFixture.runVueConfig(config(ext)) {
//        val cg = VueScriptCodeGenerator()
//        val tg = translationGenerator(ext)!!
//        myFixture.tempDirFixture.findOrCreateDir("locales")
//        myFixture.configureByText("simple.${cg.ext()}", cg.generateBlock("\"I want<caret> to move it to translation\""))
//        val action = myFixture.findSingleIntention(hint)
//        assertNotNull(action)
//        Messages.setTestInputDialog(predefinedTextInputDialog("component.header.title"))
//        myFixture.launchAction(action)
//        myFixture.checkResult(cg.generateBlock("this.\$t('component.header.title')"))
//        myFixture.checkResult(
//            "locales/en.${tg.ext()}",
//            tg.generateContent("component", "header", "title", "I want to move it to translation"),
//            false
//        )
//    }
//
//    @Test
//    fun testTranslationFileGenerationVueTs() = myFixture.runVueConfig(config("json")) {
//        val cg = VueTsCodeGenerator()
//        val tg = translationGenerator("json")!!
//        myFixture.tempDirFixture.findOrCreateDir("locales")
//        myFixture.configureByText("simple.${cg.ext()}", cg.generateBlock("\"I want<caret> to move it to translation\""))
//        val action = myFixture.findSingleIntention(hint)
//        assertNotNull(action)
//        Messages.setTestInputDialog(predefinedTextInputDialog("component.header.title"))
//        myFixture.launchAction(action)
//        myFixture.checkResult(cg.generateBlock("this.\$t('component.header.title').toString()"))
//        myFixture.checkResult(
//            "locales/en.${tg.ext()}",
//            tg.generateContent("component", "header", "title", "I want to move it to translation"),
//            false
//        )
//    }
//
//    @Test
//    fun testTranslationFileGenerationVueAttributeFix() = myFixture.runVueConfig(config("json")) {
//        val cg = VueScriptAttributeCodeGenerator("attr")
//        val tg = translationGenerator("json")!!
//        myFixture.tempDirFixture.findOrCreateDir("locales")
//        myFixture.configureByText("simple.${cg.ext()}", cg.generate("\"I want<caret> to move it to translation\""))
//        val action = myFixture.findSingleIntention(hint)
//        assertNotNull(action)
//        Messages.setTestInputDialog(predefinedTextInputDialog("component.header.title"))
//        myFixture.launchAction(action)
//        myFixture.checkResult(VueScriptAttributeCodeGenerator(":attr").generate("\"\$t('component.header.title')\""))
//        myFixture.checkResult(
//                "locales/en.${tg.ext()}",
//                tg.generateContent("component", "header", "title", "I want to move it to translation"),
//                false
//        )
//    }
}