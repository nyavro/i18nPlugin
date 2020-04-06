package ide.inspections

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class CodeHighlightingTsxTest : BasePlatformTestCase() {

    private val translation = "assets/test.json"

    override fun getTestDataPath(): String {
        return "src/test/resources/codeHighlighting"
    }

    override fun setUp() {
        super.setUp()
        val settings = Settings.getInstance(myFixture.project)
        settings.vueDirectory = "assets"
        settings.defaultNs = "translation"
        settings.vue = false
    }

    private fun check(filePath: String) {
        myFixture.configureByFiles(filePath)
        myFixture.checkHighlighting(true, false, true, true)
    }

    private fun check(filePath: String, assetPath: String, vue: Boolean = false) {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = vue
        myFixture.configureByFiles(filePath, assetPath)
        myFixture.checkHighlighting(true, true, true, true)
        settings.vue = false
    }

    fun testUnresolvedNs() {
        check("tsx/unresolvedNs.tsx")
        check("ts/unresolvedNs.ts")
        check("jsx/unresolvedNs.jsx")
        check("js/unresolvedNs.js")
        check("php/unresolvedNs.php")
    }

    fun testUnresolvedKey() {
        check("tsx/unresolvedKey.tsx", translation)
        check("ts/unresolvedKey.ts", translation)
        check("jsx/unresolvedKey.jsx", translation)
        check("js/unresolvedKey.js", translation)
        check("php/unresolvedKey.php", translation)
    }

    fun testReferenceToObject() {
        check("tsx/refToObject.tsx", translation)
        check("ts/refToObject.ts", translation)
        check("jsx/refToObject.jsx", translation)
        check("js/refToObject.js", translation)
        check("php/refToObject.php", translation)
        check("vue/refToObject.vue", "assets/en-US.json", true)
    }

    fun testReferenceToObjectDefaultNs() {
        check("tsx/refToObjectDef.tsx", "assets/translation.json")
        check("ts/refToObjectDef.ts", "assets/translation.json")
        check("jsx/refToObjectDef.jsx", "assets/translation.json")
        check("js/refToObjectDef.js", "assets/translation.json")
        check("php/refToObjectDef.php", "assets/translation.json")
    }

    fun testResolved() {
        check("tsx/resolved.tsx", translation)
        check("ts/resolved.ts", translation)
        check("jsx/resolved.jsx", translation)
        check("js/resolved.js", translation)
        check("php/resolved.php", translation)
        check("vue/resolved.vue", "assets/test.json", true)
    }

    fun testDefaultNsUnresolved() {
        check("tsx/defNsUnresolved.tsx", translation)
        check("ts/defNsUnresolved.ts", translation)
        check("jsx/defNsUnresolved.jsx", translation)
        check("js/defNsUnresolved.js", translation)
        check("php/defNsUnresolved.php", translation)
        check("vue/unresolvedKey.vue", "assets/en-US.json", true)
    }

    fun testNotTranslationArgument() {
        check("tsx/notArg.tsx", translation)
        check("ts/notArg.ts", translation)
        check("jsx/notArg.jsx", translation)
        check("js/notArg.js", translation)
        check("php/notArg.php", translation)
        check("vue/notArg.vue", "assets/en-US.json", true)
    }
}