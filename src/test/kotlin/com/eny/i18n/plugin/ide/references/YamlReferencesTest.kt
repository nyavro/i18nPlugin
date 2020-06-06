package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReference
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.code.JsxCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal abstract class TranslationToCodeTestBase(
        protected val translationGenerator: TranslationGenerator,
        protected val codeGenerator: CodeGenerator
) : BasePlatformTestCase() {

    fun testSingleReference() {
        val key = "'test:ref.section.key'"
        myFixture.addFileToProject(
            "testReference.${codeGenerator.extension()}",
            codeGenerator.generate(key)
        )
        myFixture.configureByText(
            "test.${translationGenerator.extension()}",
            translationGenerator.generateContent("ref", "section", "key<caret>", "Translation at ref section key")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(key.unQuote(), element!!.references[0].resolve()?.text?.unQuote())
    }

    fun testInvalidYaml() {
        myFixture.configureByText("invalid.${translationGenerator.extension()}", "item<caret> text")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }

    fun testNoReference() {
        myFixture.configureByText(
            "test.${translationGenerator.extension()}",
            translationGenerator.generateContent("ref", "section", "key<caret>", "Translation at ref section key")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }

    fun testMultipleReferences() {
        val key = "'multiTest:ref.section.subsection1.key12'"
        myFixture.configureByText(
            "multiReference1.${codeGenerator.extension()}",
            codeGenerator.multiGenerate(
                key,
                "skip-multiTest:ref.section.subsection1.key12",
                key
            )
        )
        myFixture.configureByText(
            "multiReference2.${codeGenerator.extension()}",
            codeGenerator.multiGenerate(
                key,
                "skip-multiTest2:ref.section.subsection1.key12"
            )
        )
        myFixture.configureByText(
            "multiTest.${translationGenerator.extension()}",
            translationGenerator.generateContent("ref", "section", "subsection1", "key1<caret>2", "Translation")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        assertEquals(setOf(key.unQuote()), (ref as TranslationToCodeReference).findRefs().map { item -> item.text.unQuote()}.toSet())
    }

    fun testObjectReference() {
        myFixture.addFileToProject(
            "testReference.${codeGenerator.extension()}",
            codeGenerator.multiGenerate(
                "'skip-objectRef:ref.section.key1'",
                "'objectRef:ref.section.key2'",
                "`skip-objectRef:ref.section.\${key2}`",
                "'objectRef:ref.section.key1'",
                "'skip-objectRef:ref.section.key2'",
                "`objectRef:ref.section.\${key2}`"
            )
        )
        myFixture.configureByText(
            "objectRef.${translationGenerator.extension()}",
            translationGenerator.generateContent("ref", "section<caret>", "key1", "val 1")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        val refs = (ref as TranslationToCodeReference).findRefs().map { item -> item.text}.toSet()
        assertEquals(
            setOf("'objectRef:ref.section.key1'", "'objectRef:ref.section.key2'", "`objectRef:ref.section.\${key2}`"),
            refs
        )
    }

    fun testDefaultNs() = myFixture.runWithConfig(Config(defaultNs = "Common")) {
        myFixture.addFileToProject(
            "testDefaultNs.${codeGenerator.extension()}",
            codeGenerator.multiGenerate(
                "'objectRef:ref.section.key1'",
                "'ref.section.key2'",
                "`objectRef:ref.section.\${key3}`",
                "'ref.section.key1'",
                "'objectRef:ref.section.key2'",
                "`ref.section.\${key3}`"
            )
        )
        myFixture.configureByText(
            "Common.${translationGenerator.extension()}",
            translationGenerator.generateContent("ref", "section<caret>", "key1", "val 1")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        val refs = (ref as TranslationToCodeReference).findRefs().map { item -> item.text}.toSet()
        assertEquals(
            setOf("'ref.section.key1'", "'ref.section.key2'", "`ref.section.\${key3}`"),
            refs
        )
    }

    fun testVue() = myFixture.runVueConfig(Config(vueDirectory = "assets")) {
        myFixture.configureByFiles("assets/en-US.${translationGenerator.extension()}", "vue/test.vue")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        assertEquals(
                setOf("'ref.section.key2'", "'ref.section.key5'"),
                (ref as TranslationToCodeReference).findRefs().map { item -> item.text}.toSet()
        )
    }

    fun testVueIncorrectConfiguration() = myFixture.runVueConfig(Config(vueDirectory = "translations")) {
        myFixture.configureByFiles("assets/en-US.${translationGenerator.extension()}", "vue/test.vue")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }
}

internal class YamlReferencesTest : TranslationToCodeTestBase(YamlTranslationGenerator(), JsxCodeGenerator()) {

    override fun getTestDataPath(): String {
        return "src/test/resources/yamlReferences"
    }

    fun testSingleReferenceQuoted() {
        val key = "'testQuoted:ref.section.key'"
        myFixture.addFileToProject(
            "testReferenceQuoted.${codeGenerator.extension()}",
            codeGenerator.generate(key)
        )
        myFixture.configureByText(
            "testQuoted.${translationGenerator.extension()}",
            translationGenerator.generateContent("ref", "section", "\"key<caret>\"", "Reference in yaml")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(key, element!!.references[0].resolve()?.text)
    }
}

internal class JsonReferencesTest : TranslationToCodeTestBase(JsonTranslationGenerator(), JsxCodeGenerator()) {

    override fun getTestDataPath(): String {
        return "src/test/resources/jsonReferences"
    }

    fun testValue() {
        myFixture.configureByText("testValue.json", translationGenerator.generateContent("ref", "section", "key", "transla<caret>tion"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }
}
