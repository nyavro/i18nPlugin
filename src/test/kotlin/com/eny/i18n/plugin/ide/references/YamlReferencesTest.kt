package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.ide.references.TranslationToCodeReference
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal abstract class TranslationToCodeTestBase(private val assetExt:String, private val ext:String) : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = false
    }

    fun testSingleReference() {
        myFixture.configureByFiles("assets/test.$assetExt", "$ext/testReference.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("'test:ref.section.key'", element!!.references[0].resolve()?.text)
    }

    fun testInvalidYaml() {
        myFixture.configureByFiles("assets/invalid.$assetExt")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }

    fun testNoReference() {
        myFixture.configureByFiles("assets/test.$assetExt")
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
        myFixture.configureByFiles("assets/multiTest.$assetExt", "jsx/testMultiReference1.$ext", "jsx/testMultiReference2.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        val refs = (ref as TranslationToCodeReference).findRefs().map { item -> item.text}.toSet()
        assertEquals(
                setOf("'multiTest:ref.section.subsection1.key12'"),
                refs
        )
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

    fun testDefaultNs() {
        val settings = Settings.getInstance(myFixture.project)
        settings.defaultNs = "Common"
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

    fun testVue() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vueDirectory = "assets"
        settings.vue = true
        myFixture.configureByFiles("assets/en-US.$assetExt", "vue/test.vue")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        assertEquals(
                setOf("'ref.section.key2'", "'ref.section.key5'"),
                (ref as TranslationToCodeReference).findRefs().map { item -> item.text}.toSet()
        )
    }

    fun testVueIncorrectConfiguration() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vueDirectory = "translations"
        settings.vue = true
        myFixture.configureByFiles("assets/en-US.$assetExt", "vue/test.vue")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }
}

internal class YamlReferencesTest : TranslationToCodeTestBase("yml", "jsx") {

    override fun getTestDataPath(): String {
        return "src/test/resources/yamlReferences"
    }
}

internal class JsonReferencesTest : TranslationToCodeTestBase("json", "jsx") {

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
