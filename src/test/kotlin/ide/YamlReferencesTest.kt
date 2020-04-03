package ide

import com.eny.i18n.plugin.ide.references.TranslationToCodeReference
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class YamlReferencesTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/yamlReferences"
    }

    private val translation = "assets/test.yml"

    fun testSingleReference() {
        myFixture.configureByFiles(translation, "jsx/testReference.jsx")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("'test:ref.section.key'", element!!.references[0].resolve()?.text)
    }

    fun testInvalidYaml() {
        myFixture.configureByFiles("assets/invalid.yml")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }

    fun testNoReference() {
        myFixture.configureByFiles("assets/test.yml")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }

    fun testSingleReferenceQuoted() {
        myFixture.configureByFiles("assets/testQuoted.yml", "jsx/testReferenceQuoted.jsx")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("'testQuoted:ref.section.key'", element!!.references[0].resolve()?.text)
    }

    fun testMultipleReferences() {
        myFixture.configureByFiles("assets/multiTest.yml", "jsx/testMultiReference1.jsx", "jsx/testMultiReference2.jsx")
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
        myFixture.configureByFiles("assets/objectRef.yml", "jsx/testObjectRef.jsx")
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
        myFixture.configureByFiles("assets/Common.yml", "jsx/testDefaultNs.jsx")
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
        myFixture.configureByFiles("assets/en-US.yml", "vue/test.vue")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        assertEquals(
            setOf("'ref.section.key2'", "'ref.section.key5'"),
            (ref as TranslationToCodeReference).findRefs().map { item -> item.text}.toSet()
        )
        settings.vue = false
    }
}
