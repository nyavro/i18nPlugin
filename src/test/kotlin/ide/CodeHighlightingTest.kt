package ide

import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class CodeHighlightingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/codeHighlighting"
    }

    private val translation = "assets/test.json"

    fun testUnresolvedNs() {
        myFixture.configureByFiles("tsx/unresolvedNs.tsx")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testUnresolvedKey() {
        myFixture.configureByFiles("tsx/unresolvedKey.tsx", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testReferenceToObject() {
        myFixture.configureByFiles("tsx/refToObject.tsx", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }
}
