package ide

import com.eny.i18n.plugin.ide.inspections.I18nInspection
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class CodeHighlightingTest : BasePlatformTestCase() {

    private val translation = "assets/test.json"

    override fun getTestDataPath(): String {
        return "src/test/resources/codeHighlighting"
    }

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(I18nInspection())
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = false
        settings.defaultNs = "translation"
    }

    fun testUnresolvedNs() {
        myFixture.configureByFiles("tsx/unresolvedNs.tsx")
        myFixture.checkHighlighting(true, false, true, true)
    }

    private fun checkDefaultNsUnresolvedKey(sourcePath: String, vue: Boolean = false) {
        val asset = if (vue) "assets/en-US.json" else "assets/common.json"
        val settings = Settings.getInstance(myFixture.project)
        if (vue) {
            settings.vue = true
            settings.vueDirectory = "assets"
        } else {
            settings.defaultNs = "common"
        }
        myFixture.configureByFiles(sourcePath, asset)
        myFixture.checkHighlighting(true, false, true, true)
    }

    private fun checkUnresolvedKey(sourcePath: String, vue: Boolean = false) {
        myFixture.configureByFiles(sourcePath, translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testUnresolvedKey() {
        checkUnresolvedKey("tsx/unresolvedKey.tsx")
        checkUnresolvedKey("ts/unresolvedKey.ts")
        checkUnresolvedKey("jsx/unresolvedKey.jsx")
        checkUnresolvedKey("js/unresolvedKey.js")
        checkUnresolvedKey("php/unresolvedKey.php")
    }

    fun testUresolvedKeyDefaultNs() {
        checkDefaultNsUnresolvedKey("tsx/unresolvedKeyDefaultNs.tsx")
        checkDefaultNsUnresolvedKey("ts/unresolvedKeyDefaultNs.ts")
        checkDefaultNsUnresolvedKey("jsx/unresolvedKeyDefaultNs.jsx")
        checkDefaultNsUnresolvedKey("jsx/unresolvedKeyDefaultNsAttribute.jsx")
        checkDefaultNsUnresolvedKey("js/unresolvedKeyDefaultNs.js")
        checkDefaultNsUnresolvedKey("php/unresolvedKeyDefaultNs.php")
        checkDefaultNsUnresolvedKey("vue/unresolvedKey.vue", true)
    }

    fun testReferenceToObject() {
        myFixture.configureByFiles("tsx/refToObject.tsx", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testResolved() {
        myFixture.configureByFiles("tsx/resolved.tsx", translation)
        myFixture.checkHighlighting(false, true, false, true)
    }

    fun testDefaultNsUnresolved() {
        myFixture.configureByFiles("tsx/defNsUresolved.tsx", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    private fun checkNotAKey(sourcePath: String) {
        myFixture.configureByFiles(sourcePath, translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testNotAKey() {
        checkNotAKey("tsx/notAKey.tsx")
    }
}
