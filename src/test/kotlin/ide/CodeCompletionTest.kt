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

    fun testTsNoCompletion() {
        myFixture.configureByFiles("ts/none.ts", translation)
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("ts/noneResult.ts")
    }

    fun testTsSingleCompletion() {
        myFixture.configureByFiles("ts/single.ts", translation)
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("ts/singleResult.ts")
    }

    fun testTsSingleNoDotCompletion() {
        myFixture.configureByFiles("ts/singleNoDot.ts", translation)
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("ts/singleNoDotResult.ts")
    }

    fun testJsSingleCompletion() {
        myFixture.configureByFiles("js/single.js", translation)
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("js/singleResult.js")
    }

    fun testPluralCompletion() {
        myFixture.configureByFiles("js/plural.js", translation)
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("js/pluralResult.js")
    }

    fun testInvalidCompletion() {
        myFixture.configureByFiles("js/invalid.js", translation)
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("js/invalidResult.js")
    }

    fun testVueSingleCompletion() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        settings.vueDirectory = "assets"
        myFixture.configureByFiles("vue/single.vue", "assets/en-US.json")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("vue/singleResult.vue")
    }

    fun testPhpSingleCompletion() {
        myFixture.configureByFiles("php/single.php", translation)
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("php/singleResult.php")
    }

    fun testPhpDQuoteCompletion() {
        myFixture.configureByFiles("php/dQuote.php", translation)
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("php/dQuoteResult.php")
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
