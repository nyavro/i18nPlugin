package com.eny.i18n.plugin.ide.references.code

import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.Json5TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import utils.randomOf

abstract class ReferencesTestBase(private val codeGenerator: CodeGenerator, private val translationGenerator: TranslationGenerator) : BasePlatformTestCase() {

    fun testReference() {
        myFixture.addFileToProject(
            "assets/test.${translationGenerator.ext()}",
            translationGenerator.generateContent("ref", "section", "key", "Reference in json"))
        myFixture.configureByText("resolved.${codeGenerator.ext()}", codeGenerator.generate("'test:ref.section.key<caret>'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue("Failed ${translationGenerator.ext()}, ${codeGenerator.ext()}", element!!.references.size > 0)
        assertEquals("Failed ${translationGenerator.ext()}, ${codeGenerator.ext()}","Reference in json", element!!.references[0].resolve()?.text?.unQuote())
    }

    fun testMultiReference() {
        myFixture.addFileToProject(
            "assets/en/multi.${translationGenerator.ext()}",
            translationGenerator.generateContent("main", "header", "title", "Welcome")
        )
        myFixture.addFileToProject(
            "assets/de/multi.${translationGenerator.ext()}",
            translationGenerator.generateContent("main", "header", "title", "Willkommen")
        )
        myFixture.configureByText("multiReference.${codeGenerator.ext()}", codeGenerator.generate("'multi:main.header.title<caret>'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
    }

    fun testMultiReferenceDefNs() {
        myFixture.addFileToProject(
            "assets/en/translation.${translationGenerator.ext()}",
            translationGenerator.generateContent("main", "header", "title", "Welcome")
        )
        myFixture.addFileToProject(
            "assets/de/translation.${translationGenerator.ext()}",
            translationGenerator.generateContent("main", "header", "title", "Willkommen")
        )
        myFixture.configureByText("multiReference.${codeGenerator.ext()}", codeGenerator.generate("'main.header.title<caret>'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
    }

    fun testReferenceMultiDefaultNs() = myFixture.runWithConfig(Config(defaultNs = "third,second;first ")) {
        val tg = translationGenerator
        //Resolves reference from key 'main.fruit' to three possible default ns (first,second,third):
        myFixture.addFileToProject("assets/en/first.${tg.ext()}", tg.generateContent("main","fruit", "apple"))
        myFixture.addFileToProject("assets/en/second.${tg.ext()}", tg.generateContent("main", "fruit", "orange"))
        myFixture.addFileToProject("assets/en/third.${tg.ext()}", tg.generateContent("main", "fruit", "pear"))
        myFixture.addFileToProject("assets/de/first.${tg.ext()}", tg.generateContent("main","fruit", "apfel"))
        myFixture.addFileToProject("assets/de/second.${tg.ext()}", tg.generateContent("main", "fruit", "apfelsine"))
        myFixture.addFileToProject("assets/de/third.${tg.ext()}", tg.generateContent("main", "fruit", "birne"))
        myFixture.configureByText("multiDefNs.${codeGenerator.ext()}", codeGenerator.generate("'main.fruit<caret>'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(setOf("apfel", "apfelsine", "apple", "birne", "orange", "pear"), getResolvedValues(element))
    }

    private fun getResolvedValues(element: PsiElement?) =
        (element?.reference as I18nReference).references.map { it.element?.value()?.text?.unQuote() }.toSet()

    fun testDefaultNsReference() {
        myFixture.addFileToProject(
            "assets/translation.${translationGenerator.ext()}",
            translationGenerator.generateContent("ref", "section", "key", "Default ns reference"))
        myFixture.configureByText("resolved.${codeGenerator.ext()}", codeGenerator.generate("'ref.section.key<caret>'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue("Failed ${translationGenerator.ext()}, ${codeGenerator.ext()}", element!!.references.size > 0)
        assertEquals("Failed ${translationGenerator.ext()}, ${codeGenerator.ext()}","Default ns reference", element!!.references[0].resolve()?.text?.unQuote())
    }

    fun testPartiallyResolvedReference() {
        myFixture.addFileToProject(
                "assets/test.${translationGenerator.ext()}",
                translationGenerator.generateContent("ref", "section", "key", "Default ns reference"))
        myFixture.configureByText("testPartiallyResolvedReference.${codeGenerator.ext()}", codeGenerator.generate("'test:ref.section<caret>.not.found'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.size > 0)
        assertEquals("section", element!!.references[0].resolve()?.text?.unQuote())
    }

    fun testExpressionReference() {
        myFixture.addFileToProject(
        "assets/test.${translationGenerator.ext()}",
            translationGenerator.generateContent("ref", "section", "key", "value"))
        myFixture.configureByText("testPartiallyResolvedReference.${codeGenerator.ext()}", codeGenerator.generate("`test:ref.section<caret>.\${b ? 'key' : 'key2'}`"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.size > 0)
        assertEquals("section", element!!.references[0].resolve()?.text?.unQuote())
    }

    fun testInvalidTranslationRoot() {
        myFixture.addFileToProject(
            "assets/invalidRoot.${translationGenerator.ext()}",
            translationGenerator.generateInvalidRoot())
        myFixture.configureByText("testInvalidReference.${codeGenerator.ext()}", codeGenerator.generate("`invalidRoot:ref.section<caret>.\${b ? 'key' : 'key2'}`"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEmpty(element!!.references)
    }

    fun testInvalidTranslationValue() {
        myFixture.addFileToProject(
            "assets/invalidTranslationValue.${translationGenerator.ext()}",
            translationGenerator.generateContent("ref", "section", "key", "value"))
        myFixture.configureByText("testInvalidTranslationValue.${codeGenerator.ext()}", "'invalidTranslationValue:ref.section<caret>.invalid'")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("section", element!!.references[0].resolve()?.text?.unQuote())
    }
}

class CodeReferencesTranslationTest: ReferencesTestBase(
    randomOf(
        ::JsCodeGenerator,
        ::TsCodeGenerator,
        ::JsxCodeGenerator,
        ::TsxCodeGenerator
//            ::PhpCodeGenerator
//            ::VueCodeGenerator
    )(),
    randomOf(::JsonTranslationGenerator, ::YamlTranslationGenerator, ::Json5TranslationGenerator)()
)

abstract class ReferenceTestVue(private val tg: TranslationGenerator): BasePlatformTestCase() {
    private val cg = VueCodeGenerator()

    fun testMultiReferenceDefNs() = myFixture.runVue {
        myFixture.addFileToProject(
                "assets/en/translation.${tg.ext()}",
                tg.generateContent("main", "header", "title", "Welcome")
        )
        myFixture.addFileToProject(
                "assets/de/translation.${tg.ext()}",
                tg.generateContent("main", "header", "title", "Willkommen")
        )
        myFixture.configureByText("multiReference.${cg.ext()}", cg.generate("'main.header.title<caret>'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
    }

    fun testReferenceMultiDefaultNs() = myFixture.runVueConfig(Config(defaultNs = "third,second;first ")) {
        val tg = tg
        //Resolves reference from key 'main.fruit' to three possible default ns (first,second,third):
        myFixture.addFileToProject("assets/en/first.${tg.ext()}", tg.generateContent("main","fruit", "apple"))
        myFixture.addFileToProject("assets/en/second.${tg.ext()}", tg.generateContent("main", "fruit", "orange"))
        myFixture.addFileToProject("assets/en/third.${tg.ext()}", tg.generateContent("main", "fruit", "pear"))
        myFixture.addFileToProject("assets/de/first.${tg.ext()}", tg.generateContent("main","fruit", "apfel"))
        myFixture.addFileToProject("assets/de/second.${tg.ext()}", tg.generateContent("main", "fruit", "apfelsine"))
        myFixture.addFileToProject("assets/de/third.${tg.ext()}", tg.generateContent("main", "fruit", "birne"))
        myFixture.configureByText("multiDefNs.${cg.ext()}", cg.generate("'main.fruit<caret>'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(setOf("apfel", "apfelsine", "apple", "birne", "orange", "pear"), getResolvedValues(element))
    }

    private fun getResolvedValues(element: PsiElement?) =
            (element?.reference as I18nReference).references.map { it.element?.value()?.text?.unQuote() }.toSet()

    fun testDefaultNsReference() = myFixture.runVue {
        myFixture.addFileToProject(
                "assets/translation.${tg.ext()}",
                tg.generateContent("ref", "section", "key", "Default ns reference"))
        myFixture.configureByText("resolved.${cg.ext()}", cg.generate("'ref.section.key<caret>'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue("Failed ${tg.ext()}, ${cg.ext()}", element!!.references.size > 0)
        assertEquals("Failed ${tg.ext()}, ${cg.ext()}","Default ns reference", element!!.references[0].resolve()?.text?.unQuote())
    }
}

class VueRandomTest: ReferenceTestVue(
    randomOf(::Json5TranslationGenerator, ::JsonTranslationGenerator, ::YamlTranslationGenerator)()
)
