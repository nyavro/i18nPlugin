package ide

import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class YamlReferencesTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/yamlReferences"
    }

    private val translation = "assets/test.yml"

    fun testReference() {
        myFixture.configureByFiles(translation, "jsx/testReference.jsx")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("'test:ref.section.key'", element!!.references[0].resolve()?.text)
    }
}
