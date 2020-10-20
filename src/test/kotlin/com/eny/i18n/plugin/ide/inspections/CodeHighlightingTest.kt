package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import org.junit.jupiter.api.Test
import utils.randomOf

abstract class CodeHighlightingTestBase(protected val codeGenerator: CodeGenerator, protected val translationGenerator: TranslationGenerator): PlatformBaseTest() {
    private val testConfig = Config(vueDirectory = "assets", defaultNs = "translation")

    protected fun check(fileName: String, code: String, translationName: String, translation: String) = myFixture.runWithConfig(testConfig) {
        myFixture.addFileToProject(translationName, translation)
        myFixture.configureByText(fileName, code)
        myFixture.checkHighlighting(true, true, true, true)
    }

    @Test
    fun testReferenceToObject() = check(
        "refToObject.${codeGenerator.ext()}",
        codeGenerator.generate("\"test:<warning descr=\"Reference to object\">tst2.plurals</warning>\""),
        "test.${translationGenerator.ext()}",
        translationGenerator.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

    @Test
    fun testReferenceToObjectDefaultNs() = check(
        "refToObjectDefNs.${codeGenerator.ext()}",
        codeGenerator.generate("\"<warning descr=\"Reference to object\">tst2.plurals</warning>\""),
        "assets/translation.${translationGenerator.ext()}",
        translationGenerator.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

    @Test
    fun testResolved() = check(
        "resolved.${codeGenerator.ext()}",
        codeGenerator.generate("\"test:tst1.base.single\""),
        "assets/translation.${translationGenerator.ext()}",
        translationGenerator.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

    @Test
    fun testNotArg() = check(
        "defNsUnresolved.${codeGenerator.ext()}",
        codeGenerator.generateInvalid(
            "\"test:tst1.base5.single\""
        ),
        "assets/test.${translationGenerator.ext()}",
        translationGenerator.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

    @Test
    fun testExpressionInsideTranslation() = check(
        "expressionInTranslation.${codeGenerator.ext()}",
        codeGenerator.generate("isSelected ? \"test:<warning descr=\"Reference to object\">tst2.plurals</warning>\" : \"test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\""),
        "test.${translationGenerator.ext()}",
        translationGenerator.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )
}

abstract class JsDialectCodeHighlightingTestBase(codeGenerator: CodeGenerator, translationGenerator: TranslationGenerator): CodeHighlightingTestBase(codeGenerator, translationGenerator) {

    @Test
    fun testDefNsUnresolved() = check(
        "defNsUnresolved.${codeGenerator.ext()}",
        codeGenerator.multiGenerate(
            "\"<warning descr=\"Missing default translation file\">missing.default.translation</warning>\"",
            "`<warning descr=\"Missing default translation file\">missing.default.in.\${template}</warning>`"
        ),
        "assets/test.${translationGenerator.ext()}",
        translationGenerator.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

    @Test
    fun testUnresolvedKey() = check(
        "unresolvedKey.${codeGenerator.ext()}",
        codeGenerator.multiGenerate(
            "\"test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key</warning>\"",
            "\"test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\"",
            "`test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key.\${arg}</warning>`",
            "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${arg}</warning>`",
            "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${arg}</warning>`",
            "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${b ? 'key' : 'key2'}</warning>`",
            "`test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.\${b ? 'key' : 'key2'}</warning>`"
        ),
        "test.${translationGenerator.ext()}",
        translationGenerator.generateContent("tst1", "base", "single", "only one value")
    )

    @Test
    fun testUnresolvedNs() = check(
        "unresolvdNs.${codeGenerator.ext()}",
        codeGenerator.multiGenerate(
            "\"<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base\"",
            "`<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base.\${arg}`"
        ),
        "test.${translationGenerator.ext()}",
        translationGenerator.generateContent("root", "first", "key", "value")
    )

    @Test
    fun testResolvedTemplate() = check(
        "resolvedTemplate.${codeGenerator.ext()}",
        codeGenerator.generate("`test:tst1.base.\${arg}`"),
        "assets/translation.${translationGenerator.ext()}",
        translationGenerator.generateContent("tst1", "base", "value", "translation")
    )
}

abstract class PhpHighlightingTest(translationGenerator: TranslationGenerator): CodeHighlightingTestBase(PhpCodeGenerator(), translationGenerator) {

    @Test
    fun testDefNsUnresolved() = check(
        "defNsUnresolved.${codeGenerator.ext()}",
        codeGenerator.multiGenerate(
                "\"<warning descr=\"Missing default translation file\">missing.default.translation</warning>\"",
                "'<warning descr=\"Missing default translation file\">missing.default.in.translation</warning>'"
        ),
        "assets/test.${translationGenerator.ext()}",
        translationGenerator.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

    @Test
    fun testUnresolvedKey() = check(
        "unresolvedKey.${codeGenerator.ext()}",
        codeGenerator.multiGenerate(
            "\"test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key</warning>\"",
            "\"test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\"",
            "'test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key</warning>'",
            "'test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>'"
        ),
        "test.${translationGenerator.ext()}",
        translationGenerator.generateContent("tst1", "base", "single", "only one value")
    )

    @Test
    fun testUnresolvedNs() = check(
        "unresolvdNs.${codeGenerator.ext()}",
        codeGenerator.multiGenerate(
            "\"<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base\"",
            "'<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base'"
        ),
        "test.${translationGenerator.ext()}",
        translationGenerator.generateContent("root", "first", "key", "value")
    )

}

class JsDialectCodeHighlightingRandomTest: JsDialectCodeHighlightingTestBase(
        randomOf(
                ::JsCodeGenerator,
                ::TsCodeGenerator,
                ::JsxCodeGenerator,
                ::TsxCodeGenerator)(),
        randomOf(
                ::JsonTranslationGenerator,
                ::YamlTranslationGenerator)()
)

class PhpCodeHighlightingRandomTest: PhpHighlightingTest(
        randomOf(
                ::JsonTranslationGenerator,
                ::YamlTranslationGenerator)()
)