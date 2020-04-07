package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
internal class CodeCompletionTest : BasePlatformTestCase() {

    private val translation = "assets/test.json"

    override fun getTestDataPath(): String {
        return "src/test/resources/codeCompletion"
    }

    override fun setUp() {
        super.setUp()
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = false
    }

    private fun check(filePath: String, expectedFilePath: String) {
        myFixture.configureByFiles(filePath, translation)
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile(expectedFilePath)
    }

    private fun checkDefault(filePath: String, expectedFilePath: String) {
        myFixture.configureByFiles(filePath, "assets/translation.json")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile(expectedFilePath)
    }

    private fun checkVue(filePath: String, expectedFilePath: String) {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        settings.vueDirectory = "assets"
        myFixture.configureByFiles(filePath, "assets/en-US.json")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile(expectedFilePath)
    }

    fun testNsCompletion() {
        check("ts/none.ts", "ts/noneResult.ts")
        check("ts/single.ts", "ts/singleResult.ts")
        check("ts/plural.ts", "ts/pluralResult.ts")
        check("ts/singleNoDot.ts", "ts/singleNoDotResult.ts")
        check("tsx/none.tsx", "tsx/noneResult.tsx")
        check("tsx/single.tsx", "tsx/singleResult.tsx")
        check("tsx/plural.tsx", "tsx/pluralResult.tsx")
        check("tsx/singleNoDot.tsx", "tsx/singleNoDotResult.tsx")
        check("js/none.js", "js/noneResult.js")
        check("js/single.js", "js/singleResult.js")
        check("js/plural.js", "js/pluralResult.js")
        check("js/invalid.js", "js/invalidResult.js")
        check("jsx/none.jsx", "jsx/noneResult.jsx")
        check("jsx/single.jsx", "jsx/singleResult.jsx")
        check("jsx/plural.jsx", "jsx/pluralResult.jsx")
        check("jsx/singleNoDot.jsx", "jsx/singleNoDotResult.jsx")
        check("php/single.php", "php/singleResult.php")
        check("php/plural.php", "php/pluralResult.php")
        check("php/singleNoDot.php", "php/singleNoDotResult.php")
        check("php/dQuote.php", "php/dQuoteResult.php")
    }

    fun testDefaultCompletion() {
        checkDefault("ts/default/none.ts", "ts/default/noneResult.ts")
        checkDefault("ts/default/single.ts", "ts/default/singleResult.ts")
        checkDefault("ts/default/plural.ts", "ts/default/pluralResult.ts")
        checkDefault("ts/default/singleNoDot.ts", "ts/default/singleNoDotResult.ts")
        checkDefault("tsx/default/none.tsx", "tsx/default/noneResult.tsx")
        checkDefault("tsx/default/single.tsx", "tsx/default/singleResult.tsx")
        checkDefault("tsx/default/plural.tsx", "tsx/default/pluralResult.tsx")
        checkDefault("tsx/default/singleNoDot.tsx", "tsx/default/singleNoDotResult.tsx")
        checkDefault("js/default/none.js", "js/default/noneResult.js")
        checkDefault("js/default/single.js", "js/default/singleResult.js")
        checkDefault("js/default/plural.js", "js/default/pluralResult.js")
        checkDefault("js/default/invalid.js", "js/default/invalidResult.js")
        checkDefault("jsx/default/none.jsx", "jsx/default/noneResult.jsx")
        checkDefault("jsx/default/single.jsx", "jsx/default/singleResult.jsx")
        checkDefault("jsx/default/plural.jsx", "jsx/default/pluralResult.jsx")
        checkDefault("jsx/default/singleNoDot.jsx", "jsx/default/singleNoDotResult.jsx")
        checkDefault("php/default/single.php", "php/default/singleResult.php")
        checkDefault("php/default/plural.php", "php/default/pluralResult.php")
        checkDefault("php/default/singleNoDot.php", "php/default/singleNoDotResult.php")
        checkDefault("php/default/dQuote.php", "php/default/dQuoteResult.php")
    }

    fun testVueSingleCompletion() {
        checkVue("vue/single.vue", "vue/singleResult.vue")
        checkVue("vue/singleNoDot.vue", "vue/singleNoDotResult.vue")
    }

//
//    fun testFormatter() {
//        myFixture.configureByFiles("FormatterTestData.simple")
//        CodeStyle.getLanguageSettings(myFixture.file).SPACE_AROUND_ASSIGNMENT_OPERATORS = true
//        CodeStyle.getLanguageSettings(myFixture.file).KEEP_BLANK_LINES_IN_CODE = 2
//        WriteCommandAction.writeCommandAction(project).run<RuntimeException> {
//            CodeStyleManager.getInstance(project).reformatText(myFixture.file,
//                    ContainerUtil.newArrayList(myFixture.file.textRange))
//        }
//        myFixture.checkResultByFile("DefaultTestData.simple")
//    }
//
//    fun testRename() {
//        myFixture.configureByFiles("RenameTestData.java", "RenameTestData.simple")
//        myFixture.renameElementAtCaret("websiteUrl")
//        myFixture.checkResultByFile("RenameTestData.simple", "RenameTestDataAfter.simple", false)
//    }
//
//    fun testFolding() {
//        myFixture.configureByFiles("DefaultTestData.simple")
//        myFixture.testFolding("$testDataPath/FoldingTestData.java")
//    }
//
//    fun testFindUsages() {
//         val usageInfos = myFixture.testFindUsages("FindUsagesTestData.simple", "FindUsagesTestData.java")
//        Assert.assertEquals(1, usageInfos.size)
//    }
//
//    fun testCommenter() {
//        myFixture.configureByText(SimpleFileType.INSTANCE, "<caret>website = http://en.wikipedia.org/")
//        val commentAction = CommentByLineCommentAction()
//        commentAction.actionPerformedImpl(project, myFixture.editor)
//        myFixture.checkResult("#website = http://en.wikipedia.org/")
//        commentAction.actionPerformedImpl(project, myFixture.editor)
//        myFixture.checkResult("website = http://en.wikipedia.org/")
//    }
}
