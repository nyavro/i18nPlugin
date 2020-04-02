package ide

import com.eny.i18n.plugin.ide.JsonI18nReference
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
        assertTrue(ref is JsonI18nReference)
        val refs = (ref as JsonI18nReference).findRefs().map { item -> item.text}.toSet()
        assertEquals(
            setOf("'multiTest:ref.section.subsection1.key12'"),
            refs
        )
    }

    fun testObjectReference() {
        myFixture.configureByFiles("assets/objectRef.yml", "jsx/testObjectRef.jsx")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is JsonI18nReference)
        val refs = (ref as JsonI18nReference).findRefs().map { item -> item.text}.toSet()
        assertEquals(
            setOf("'objectRef:ref.section.key1'", "'objectRef:ref.section.key2'", "`objectRef:ref.section.\${key2}`"),
            refs
        )
    }
}
