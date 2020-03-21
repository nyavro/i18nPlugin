package ide

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

    fun testReferenceToObjectDefaultNs() {
        myFixture.configureByFiles("tsx/refToObjectDef.tsx", "assets/translation.json")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testResolved() {
        myFixture.configureByFiles("tsx/resolved.tsx", translation)
        myFixture.checkHighlighting(false, true, false, true)
    }

    fun testDefaultNsUnresolved() {
        myFixture.configureByFiles("tsx/defNsUresolved.tsx", translation)
        myFixture.checkHighlighting(true, true, true, false)
    }
}

internal class CodeHighlightingTsTest : BasePlatformTestCase() {

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
        myFixture.configureByFiles("ts/unresolvedNs.ts")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testUnresolvedKey() {
        myFixture.configureByFiles("ts/unresolvedKey.ts", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testReferenceToObject() {
        myFixture.configureByFiles("ts/refToObject.ts", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testReferenceToObjectDefaultNs() {
        myFixture.configureByFiles("ts/refToObjectDef.ts", "assets/translation.json")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testResolved() {
        myFixture.configureByFiles("ts/resolved.ts", translation)
        myFixture.checkHighlighting(false, true, false, true)
    }

    fun testDefaultNsUnresolved() {
        myFixture.configureByFiles("ts/defNsUnresolved.ts", translation)
        myFixture.checkHighlighting(true, true, true, false)
    }
}

internal class CodeHighlightingJsTest : BasePlatformTestCase() {

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
        myFixture.configureByFiles("js/unresolvedNs.js")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testUnresolvedKey() {
        myFixture.configureByFiles("js/unresolvedKey.js", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testReferenceToObject() {
        myFixture.configureByFiles("js/refToObject.js", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testReferenceToObjectDefaultNs() {
        myFixture.configureByFiles("js/refToObjectDef.js", "assets/translation.json")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testResolved() {
        myFixture.configureByFiles("js/resolved.js", translation)
        myFixture.checkHighlighting(false, true, false, true)
    }

    fun testDefaultNsUnresolved() {
        myFixture.configureByFiles("js/defNsUnresolved.js", translation)
        myFixture.checkHighlighting(true, true, true, false)
    }
}

internal class CodeHighlightingJsxTest : BasePlatformTestCase() {

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
        myFixture.configureByFiles("jsx/unresolvedNs.jsx")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testUnresolvedKey() {
        myFixture.configureByFiles("jsx/unresolvedKey.jsx", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testReferenceToObject() {
        myFixture.configureByFiles("jsx/refToObject.jsx", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testReferenceToObjectDefaultNs() {
        myFixture.configureByFiles("jsx/refToObjectDef.jsx", "assets/translation.json")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testResolved() {
        myFixture.configureByFiles("jsx/resolved.jsx", translation)
        myFixture.checkHighlighting(false, true, false, true)
    }

    fun testDefaultNsUnresolved() {
        myFixture.configureByFiles("jsx/defNsUresolved.jsx", translation)
        myFixture.checkHighlighting(true, true, true, false)
    }
}

internal class CodeHighlightingPhpTest : BasePlatformTestCase() {

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
        myFixture.configureByFiles("php/unresolvedNs.php")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testUnresolvedKey() {
        myFixture.configureByFiles("php/unresolvedKey.php", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testReferenceToObject() {
        myFixture.configureByFiles("php/refToObject.php", translation)
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testReferenceToObjectDefaultNs() {
        myFixture.configureByFiles("php/refToObjectDef.php", "assets/translation.json")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testResolved() {
        myFixture.configureByFiles("php/resolved.php", translation)
        myFixture.checkHighlighting(false, true, false, true)
    }

    fun testDefaultNsUnresolved() {
        myFixture.configureByFiles("php/defNsUnresolved.php", translation)
        myFixture.checkHighlighting(true, true, true, false)
    }
}

internal class CodeHighlightingVueTest : BasePlatformTestCase() {

    private val translation = "assets/test.json"

    override fun getTestDataPath(): String {
        return "src/test/resources/codeHighlighting"
    }

    override fun setUp() {
        super.setUp()
        val settings = Settings.getInstance(myFixture.project)
        settings.vueDirectory = "assets"
        settings.vue = true
    }

    fun testResolved() {
        myFixture.configureByFiles("vue/resolved.vue", translation)
        myFixture.checkHighlighting(false, true, false, true)
    }

    fun testDefaultNsUnresolved() {
        myFixture.configureByFiles("vue/unresolvedKey.vue", "assets/en-US.json")
        myFixture.checkHighlighting(true, false, true, true)
    }

    fun testReferenceToObject() {
        myFixture.configureByFiles("vue/refToObject.vue", "assets/en-US.json")
        myFixture.checkHighlighting(true, false, true, true)
    }
}