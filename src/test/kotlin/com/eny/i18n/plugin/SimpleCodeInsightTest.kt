package com.eny.i18n.plugin

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class SimpleCodeInsightTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources"
    }

    fun testCompletion() {
        val cfg = myFixture.configureByFiles("Test.ts", "test.json")
        myFixture.complete(CompletionType.BASIC, 1)
        val strings = myFixture.lookupElementStrings
    }
//
//    fun testAnnotator() {
//        myFixture.configureByFiles("AnnotatorTestData.java", "DefaultTestData.simple")
//        myFixture.checkHighlighting(false, false, true, true)
//    }
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
//        val usageInfos = myFixture.testFindUsages("FindUsagesTestData.simple", "FindUsagesTestData.java")
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
//
//    fun testReference() {
//        myFixture.configureByFiles("ReferenceTestData.java", "DefaultTestData.simple")
//        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
//        assertEquals("http://en.wikipedia.org/", (element.references[0].resolve() as SimpleProperty?).getValue())
//    }
}
