package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.utils.generator.code.*
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class CodeCompletionInvalidTestBase(private val codeGenerator: CodeGenerator): BasePlatformTestCase() {

    fun testInvalid() {
        myFixture.configureByText("invalid.js", codeGenerator.generate("\"test: tst2:.test.<caret>\""))
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResult(codeGenerator.generate("\"test: tst2:.test.\""))
    }

    fun testInvalidDefNs() {
        myFixture.configureByText("invalidDefNs.js", codeGenerator.generate("\" tst2:.test.<caret>\""))
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResult(codeGenerator.generate("\" tst2:.test.\""))
    }
}

class JsCodeCompletionInvalidTest: CodeCompletionInvalidTestBase(JsCodeGenerator())
class TsCodeCompletionInvalidTest: CodeCompletionInvalidTestBase(TsCodeGenerator())
class JsxCodeCompletionInvalidTest: CodeCompletionInvalidTestBase(JsxCodeGenerator())
class TsxCodeCompletionInvalidTest: CodeCompletionInvalidTestBase(TsxCodeGenerator())
class PhpCodeCompletionInvalidTest: CodeCompletionInvalidTestBase(PhpCodeGenerator())
