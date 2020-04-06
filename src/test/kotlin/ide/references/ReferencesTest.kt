package ide.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class ReferencesTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/references"
    }

    private val translation = "assets/test.json"

    fun testReference() {
        myFixture.configureByFiles("jsx/testReference.jsx", translation)
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("\"Reference in json\"", element!!.references[0].resolve()?.text)
    }

    fun testExpressionReference() {
        myFixture.configureByFiles("tsx/testReference.tsx", translation)
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("\"section\"", element!!.references[0].resolve()?.text)
    }
}
