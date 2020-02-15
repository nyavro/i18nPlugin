package ide

import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class CodeHighlightingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/codeHighlighting"
    }

    fun testTsNoCompletion() {
        myFixture.configureByFiles("tsx/unresolvedNs.tsx")
        myFixture.checkHighlighting(false, false, true, false)
    }
}
