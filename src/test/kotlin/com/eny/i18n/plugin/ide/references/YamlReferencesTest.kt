package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReference
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import com.eny.i18n.plugin.utils.unQuote
import org.junit.jupiter.api.Test

abstract class TranslationToCodeTestBase(
        protected val translationGenerator: TranslationGenerator,
        protected val codeGenerator: CodeGenerator
) : PlatformBaseTest() {

    @Test
    fun testSingleReference() {
        val key = "'test:ref.section.key'"
        myFixture.addFileToProject(
            "testReference.${codeGenerator.ext()}",
            codeGenerator.generate(key)
        )
        myFixture.configureByText(
            "test.${translationGenerator.ext()}",
            translationGenerator.generateContent("ref", "section", "key<caret>", "Translation at ref section key")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(key.unQuote(), element!!.references[0].resolve()?.text?.unQuote())
    }

    @Test
    fun testInvalidYaml() {
        myFixture.configureByText("invalid.${translationGenerator.ext()}", "item<caret> text")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }

    @Test
    fun testNoReference() {
        myFixture.configureByText(
            "test.${translationGenerator.ext()}",
            translationGenerator.generateContent("ref", "section", "key<caret>", "Translation at ref section key")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }

    @Test
    fun testMultipleReferences() {
        val key = "'multiTest:ref.section.subsection1.key12'"
        myFixture.configureByText(
            "multiReference1.${codeGenerator.ext()}",
            codeGenerator.multiGenerate(
                key,
                "skip-multiTest:ref.section.subsection1.key12",
                key
            )
        )
        myFixture.configureByText(
            "multiReference2.${codeGenerator.ext()}",
            codeGenerator.multiGenerate(
                key,
                "skip-multiTest2:ref.section.subsection1.key12"
            )
        )
        myFixture.configureByText(
            "multiTest.${translationGenerator.ext()}",
            translationGenerator.generateContent("ref", "section", "subsection1", "key1<caret>2", "Translation")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        assertEquals(setOf(key.unQuote()), (ref as TranslationToCodeReference).findRefs().map { item -> item.text.unQuote()}.toSet())
    }

    @Test
    fun testObjectReference() {
        myFixture.addFileToProject(
            "testReference.${codeGenerator.ext()}",
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
            "objectRef.${translationGenerator.ext()}",
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

    @Test
    fun testDefaultNs() = myFixture.runWithConfig(Config(defaultNs = "Common")) {
        myFixture.addFileToProject(
            "testDefaultNs.${codeGenerator.ext()}",
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
            "Common.${translationGenerator.ext()}",
            translationGenerator.generateContent("ref", "section<caret>", "key1", "val 1")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        val refs = (ref as TranslationToCodeReference).findRefs().map {item -> item.text}.toSet()
        assertEquals(
            setOf("'ref.section.key1'", "'ref.section.key2'", "`ref.section.\${key3}`"),
            refs
        )
    }

    @Test
    fun testClickOnValue() {
        myFixture.configureByText(
            "testValue.${translationGenerator.ext()}",
            translationGenerator.generateContent("ref", "section", "key", "transla<caret>tion")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }
}

abstract class YamlReferencesTestBase(codeGenerator: CodeGenerator) : TranslationToCodeTestBase(YamlTranslationGenerator(), codeGenerator) {

    @Test
    fun testSingleReferenceQuoted() {
        val key = "'testQuoted:ref.section.key'"
        myFixture.addFileToProject(
            "testReferenceQuoted.${codeGenerator.ext()}",
            codeGenerator.generate(key)
        )
        myFixture.configureByText(
            "testQuoted.${translationGenerator.ext()}",
            translationGenerator.generateContent("ref", "section", "\"key<caret>\"", "Reference in yaml")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(key.unQuote(), element!!.references[0].resolve()?.text?.unQuote())
    }
}

abstract class JsonReferencesTestBase(codeGenerator: CodeGenerator) : TranslationToCodeTestBase(JsonTranslationGenerator(), codeGenerator)

abstract class VueReferencesTestBase(protected val translationGenerator: TranslationGenerator): PlatformBaseTest() {

    private val codeGenerator = VueCodeGenerator()

    private val testVue = codeGenerator.multiGenerate(
        "'skip.ref.section.key1'",
        "'ref.section.key2'",
        "'drop.ref.section.key3'",
        "'skpref.section.key4'",
        "'ref.section.key5'"
    )
    private val translation = translationGenerator.generateContent("ref", "section<caret>", "key1", "val 1")

    @Test
    fun testVue() = myFixture.runVueConfig(Config(vueDirectory = "assets")) {
        myFixture.addFileToProject("test.vue", testVue)
        myFixture.configureFromExistingVirtualFile(
            myFixture.addFileToProject("assets/en-US.${translationGenerator.ext()}", translation).virtualFile
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        assertEquals(
            setOf("'ref.section.key2'", "'ref.section.key5'"),
            (ref as TranslationToCodeReference).findRefs().map { item -> item.text}.toSet()
        )
    }

    @Test
    fun testVueIncorrectConfiguration() = myFixture.runVueConfig(Config(vueDirectory = "translations")) {
        myFixture.configureByText("test.vue", testVue)
        myFixture.configureFromExistingVirtualFile(
            myFixture.addFileToProject("assets/en-US.${translationGenerator.ext()}", translation).virtualFile
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }
}
//
class VueJsonReferencesTest: VueReferencesTestBase(JsonTranslationGenerator())
class VueYamlReferencesTest: VueReferencesTestBase(YamlTranslationGenerator())
class JsxJsonReferencesTest: JsonReferencesTestBase(JsxCodeGenerator())
class JsxYamlReferencesTest: YamlReferencesTestBase(JsxCodeGenerator())
class JsJsonReferencesTest: JsonReferencesTestBase(JsCodeGenerator())
class JsYamlReferencesTest: YamlReferencesTestBase(JsCodeGenerator())
class TsxJsonReferencesTest: JsonReferencesTestBase(TsxCodeGenerator())
class TsxYamlReferencesTest: YamlReferencesTestBase(TsxCodeGenerator())
class TsJsonReferencesTest: JsonReferencesTestBase(TsCodeGenerator())
class TsYamlReferencesTest: YamlReferencesTestBase(TsCodeGenerator())