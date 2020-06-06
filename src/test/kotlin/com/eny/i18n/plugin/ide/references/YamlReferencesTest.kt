package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReference
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.code.JsxCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.ContentGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonContentGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlContentGenerator
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal abstract class TranslationToCodeTestBase(
        private val assetExt:String,
        private val ext:String,
        private val contentGenerator: ContentGenerator,
        private val codeGenerator: CodeGenerator
) : BasePlatformTestCase() {

    fun testSingleReference() {
        val key = "test:ref.section.key"
        myFixture.addFileToProject(
            "testReference.${codeGenerator.extension()}",
            codeGenerator.generate(key)
        )
        myFixture.configureByText(
            "test.${contentGenerator.extension()}",
            contentGenerator.generateContent("ref", "section", "key<caret>", "Translation at ref section key")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(key, element!!.references[0].resolve()?.text?.unQuote())
    }

    fun testInvalidYaml() {
        myFixture.configureByFiles("assets/invalid.$assetExt")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }

    fun testNoReference() {
        myFixture.configureByText(
            "test.${contentGenerator.extension()}",
            contentGenerator.generateContent("ref", "section", "key<caret>", "Translation at ref section key")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }

    fun testSingleReferenceQuoted() {
        myFixture.configureByFiles("assets/testQuoted.$assetExt", "$ext/testReferenceQuoted.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("'testQuoted:ref.section.key'", element!!.references[0].resolve()?.text)
    }

    fun testMultipleReferences() {
        val key = "multiTest:ref.section.subsection1.key12"
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
            "multiTest.$assetExt",
            contentGenerator.generateContent("ref", "section", "subsection1", "key1<caret>2", "Translation")
        )
        myFixture.configureByFiles("assets/multiTest.$assetExt", "jsx/testMultiReference1.$ext", "jsx/testMultiReference2.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        assertEquals(setOf(key), (ref as TranslationToCodeReference).findRefs().map { item -> item.text.unQuote()}.toSet())
    }

    fun testObjectReference() {
        myFixture.configureByFiles("assets/objectRef.$assetExt", "jsx/testObjectRef.$ext")
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
        myFixture.configureByFiles("assets/Common.$assetExt", "jsx/testDefaultNs.$ext")
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
        myFixture.configureByFiles("assets/en-US.$assetExt", "vue/test.vue")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        assertEquals(
            setOf("'ref.section.key2'", "'ref.section.key5'"),
            (ref as TranslationToCodeReference).findRefs().map { item -> item.text}.toSet()
        )
    }

    fun testVueIncorrectConfiguration() = myFixture.runVueConfig(Config(vueDirectory = "translations")) {
        myFixture.configureByFiles("assets/en-US.$assetExt", "vue/test.vue")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }
}

internal class YamlReferencesTest : TranslationToCodeTestBase("yml", "jsx", YamlContentGenerator(), JsxCodeGenerator()) {

    override fun getTestDataPath(): String {
        return "src/test/resources/yamlReferences"
    }
}

internal class JsonReferencesTest : TranslationToCodeTestBase("json", "jsx", JsonContentGenerator(), JsxCodeGenerator()) {

    override fun getTestDataPath(): String {
        return "src/test/resources/jsonReferences"
    }

    fun testValue() {
        myFixture.configureByFiles("assets/testValue.json")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }
}
