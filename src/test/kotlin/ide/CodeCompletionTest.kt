package ide

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

    fun checkCompletion(srcPath: String, expectedVariants: Set<String>) {
        myFixture.configureByFiles(srcPath, translation)
        myFixture.complete(CompletionType.BASIC, 1)
        val strings = myFixture.lookupElementStrings
        assertEquals(expectedVariants, strings?.toSet())
    }

    fun testSingleCompletion() {
//        checkCompletion("ts/single.ts", setOf("single"))
        checkCompletion("ts/singleNoDot.ts", setOf("single"))
//        checkCompletion("ts/none.ts","ts/noneResult.ts")
//        checkCompletion("js/single.js", "js/singleResult.js")
//        checkCompletion("js/plural.js", "js/pluralResult.js")
//        checkCompletion("js/invalid.js", "js/invalidResult.js")
//        checkCompletion("tsx/single.tsx", "tsx/singleResult.tsx")
//        checkCompletion("tsx/singleNoDot.tsx", "tsx/singleNoDotResult.tsx")
//        checkCompletion("tsx/none.tsx","tsx/noneResult.tsx")
//        checkCompletion("jsx/single.jsx", "jsx/singleResult.jsx")
//        checkCompletion("jsx/singleNoDot.jsx", "jsx/singleNoDotResult.jsx")
//        checkCompletion("jsx/none.jsx","jsx/noneResult.jsx")
//        checkCompletion("php/single.php","php/singleResult.php")
//        checkCompletion("php/dQuote.php","php/dQuoteResult.php")
    }

    fun testVueSingleCompletion() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        settings.vueDirectory = "assets"
        myFixture.configureByFiles("vue/single.vue", "assets/en-US.json")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("vue/singleResult.vue")
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
