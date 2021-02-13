package com.eny.i18n.plugin.addons.technology.php

import com.eny.i18n.plugin.ide.completion.*
import com.eny.i18n.plugin.utils.generator.code.PhpCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.junit.Test

internal abstract class CodeCompletionTestPhpBase(
        translationGenerator: TranslationGenerator,
        keyGenerator: KeyGenerator,
        checkerProducer: (fixture: CodeInsightTestFixture) -> Checker = ::NsChecker) :
        CodeCompletionTestBase(PhpCodeGenerator(), translationGenerator, keyGenerator, checkerProducer) {

    @Test
    fun testDQuote() {
        checker.doCheck(
            "dQuote.${codeGenerator.ext()}",
            codeGenerator.generate(keyGenerator.generate("test", "tst1.base.<caret>", "\"")),
            codeGenerator.generate(keyGenerator.generate("test", "tst1.base.single", "\"")),
            translationGenerator.ext(),
            translationGenerator.generateContent("tst1", "base", "single", "only one value")
        )
    }
}

internal class CodeCompletionTest: CodeCompletionTestPhpBase(JsonTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionDefNsTest: CodeCompletionTestPhpBase(JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
