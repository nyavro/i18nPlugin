package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.PhpCodeAndTranslationGenerators
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class PhpHighlightingTestBase: PlatformBaseTest() {

    @Test
    fun testNotInContext() = myFixture.customHighlightingCheck(
            "notInContext.php",
            "function test() { \"don't try to resolve this text!\" }",
            "test.json",
            "root: {}"
    )

    @ParameterizedTest
    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
    fun testDefNsUnresolved(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customHighlightingCheck(
        "defNsUnresolved.${cg.ext()}",
        cg.multiGenerate(
                "\"<warning descr=\"Missing default translation file\">missing.default.translation</warning>\"",
                "'<warning descr=\"Missing default translation file\">missing.default.in.translation</warning>'"
        ),
        "assets/test.${tg.ext()}",
        tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

    @ParameterizedTest
    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
    fun testUnresolvedKey(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customHighlightingCheck(
        "unresolvedKey.${cg.ext()}",
        cg.multiGenerate(
            "\"test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key</warning>\"",
            "\"test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\"",
            "'test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key</warning>'",
            "'test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>'"
        ),
        "test.${tg.ext()}",
        tg.generateContent("tst1", "base", "single", "only one value")
    )

    @ParameterizedTest
    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
    fun testUnresolvedNs(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customHighlightingCheck(
        "unresolvdNs.${cg.ext()}",
        cg.multiGenerate(
            "\"<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base\"",
            "'<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base'"
        ),
        "test.${tg.ext()}",
        tg.generateContent("root", "first", "key", "value")
    )
}