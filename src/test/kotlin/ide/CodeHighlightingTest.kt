package ide

import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class CodeHighlightingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/codeHighlighting"
    }

    private val translation = "assets/test.json"

    fun testTsNoCompletion() {
        myFixture.configureByFiles("tsx/unresolvedNs.tsx")
        myFixture.checkHighlighting(false, false, true, false)
    }
}
