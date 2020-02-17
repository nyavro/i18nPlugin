package ide

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class CodeHighlightingTest : BasePlatformTestCase() {

    private val translation = "assets/test.json"

    override fun getTestDataPath(): String {
        return "src/test/resources/codeHighlighting"
    }

    override fun setUp() {
        super.setUp()
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = false
    }

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

    fun testVueUnresolvedFile() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        settings.vueDirectory = "assets"
        myFixture.configureByFiles("vue/unresolvedNs.vue")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testVueUnresolvedKey() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        settings.vueDirectory = "assets"
        myFixture.configureByFiles("vue/unresolvedKey.vue", "assets/en-US.json")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testUnresolvedKeyPhp() {
        myFixture.configureByFiles("php/unresolvedKey.php", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }
}
