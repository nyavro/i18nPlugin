package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.PlatformBaseTest
//import com.eny.i18n.plugin.ide.JsCodeAndTranslationGeneratorsNs
import com.eny.i18n.plugin.utils.PluginBundle
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.code.JsCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import org.junit.Test
//import org.junit.jupiter.api.Disabled
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.*

class CreateKeyTest: PlatformBaseTest() {
// @TODO 2

//    @ParameterizedTest
//    @ArgumentsSource(JsCodeAndTranslationGeneratorsNs::class)
//    fun testCreateKey(cg: CodeGenerator, tg: TranslationGenerator) {
//        val defaultNs = false
//        val hint = PluginBundle.getMessage("quickfix.create.key")
//        val translationFileName = (if (defaultNs) "translation" else "test") + "." + tg.ext()
//        val ns = if (defaultNs) "" else "test:"
//        myFixture.addFileToProject(
//            translationFileName,
//            contentEn(tg)
//        )
//        myFixture.configureByText("simple.${cg.ext()}", cg.generate("\"${ns}ref.section.mi<caret>ssing\""))
//        val action = myFixture.filterAvailableIntentions(hint).find {it.text == hint}!!
//        assertNotNull(action)
//        myFixture.launchAction(action)
//        myFixture.checkResult(
//            translationFileName,
//            expectedEn(tg, "${ns}ref.section.missing"),
//            false
//        )
//    }

    @Test
    fun testCreateKeyEmptyJson() {
        val cg = JsCodeGenerator()
        val tg = JsonTranslationGenerator()
        val hint = "Create i18n key"
        val translationFileName = "test" + "." + tg.ext()
        myFixture.addFileToProject(
                translationFileName,
                "{}"
        )
        myFixture.configureByText("simple.${cg.ext()}", cg.generate("\"test:ref.section.mi<caret>ssing\""))
        val action = myFixture.filterAvailableIntentions(hint).find {it.text == hint}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResult(
                translationFileName,
                """
                    {
                      "ref": {
                        "section": {
                          "missing": "test:ref.section.missing"
                        }
                      }
                    }
                """.trimIndent(),
                false
        )
    }

    @Test
    fun testCreateKeyEmptyYaml() {
        val cg = JsCodeGenerator()
        val tg = YamlTranslationGenerator()
        val hint = "Create i18n key"
        val translationFileName = "test" + "." + tg.ext()
        myFixture.addFileToProject(
                translationFileName,
                ""
        )
        myFixture.configureByText("simple.${cg.ext()}", cg.generate("\"test:ref.section.mi<caret>ssing\""))
        val action = myFixture.filterAvailableIntentions(hint).find {it.text == hint}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResult(
                translationFileName,
                """
                |  ref:
                |    section:
                |      missing: test:ref.section.missing""".trimMargin(),
                false
        )
    }

//    @Disabled
//    @ParameterizedTest
//    @ArgumentsSource(JsCodeAndTranslationGeneratorsNs::class)
//    fun createKeyMultipleTranslations(cg: CodeGenerator, tg: TranslationGenerator) {
//        val defaultNs = true
//        val translationFileName = (if (defaultNs) "translation" else "test") + "." + tg.ext()
//        val ns = if (defaultNs) "" else "test:"
//        myFixture.addFileToProject(
//            "assets/en/${translationFileName}",
//            contentEn(tg)
//        )
//        myFixture.addFileToProject(
//            "assets/ru/${translationFileName}",
//            contentRu(tg)
//        )
//        myFixture.configureByText("simple.${cg.ext()}", cg.generate("\"${ns}ref.section.mi<caret>ssing\""))
//        val action = myFixture.findSingleIntention("Create i18n key in all translation files")
//        assertNotNull(action)
//        myFixture.launchAction(action)
//        myFixture.checkResult(
//            "assets/en/${translationFileName}",
//            expectedEn(tg, "${ns}ref.section.missing"),
//            true
//        )
//        myFixture.checkResult(
//            "assets/ru/${translationFileName}",
//            expectedRu(tg, "${ns}ref.section.missing"),
//            true
//        )
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(TranslationGenerators::class)
//    fun createKeyMultipleTranslationsVue(tg: TranslationGenerator) = myFixture.runVue {
//        val cg = VueCodeGenerator()
//        myFixture.addFileToProject(
//            "locales/en-US.${tg.ext()}",
//            contentEn(tg)
//        )
//        myFixture.addFileToProject(
//            "locales/ru-RU.${tg.ext()}",
//            contentRu(tg)
//        )
//        myFixture.configureByText("simple.${cg.ext()}", cg.generate("\"ref.section.mi<caret>ssing\""))
//        var action: IntentionAction? = null
//
//            action = myFixture.findSingleIntention("Create i18n key in all translation files")
//            assertNotNull(action)
//
//        myFixture.launchAction(action!!)
//        read {
//            myFixture.checkResult(
//                "locales/en-US.${tg.ext()}",
//                tg.generateNamedBlock(
//                    "ref",
//                    expectedEn(tg)
//                ),
//                false
//            )
//            myFixture.checkResult(
//                "locales/ru-RU.${tg.ext()}",
//                tg.generateNamedBlock(
//                    "ref",
//                    expectedRu(tg)
//                ),
//                false
//            )
//        }
//    }

    data class Descr(val quot: String, val open: String, val close: String, val sep: String, val rootTab: Boolean = true)

    interface Element {
        fun build(descr: Descr, tab: String): String
    }

    class Val(private val value: String): Element {
        override fun build(descr: Descr, tab: String): String {
            return descr.quot + value + descr.quot
        }
    }

    class Obj: Element {
        private val list = LinkedList<Pair<String, Element>>()
        fun with(name: String, elem: Element): Obj {
            list.add(Pair(name, elem))
            return this
        }
        fun with(name: String, elem: String): Obj {
            list.add(Pair(name, Val(elem)))
            return this
        }

        override fun build(descr: Descr, tab: String): String {
            return """{
                |${list.map {formatItem(it, descr, tab)}.joinToString (descr.sep + "\n")}
                |$tab}""".trimMargin()
        }
        private fun formatItem(item:Pair<String, Element>, descr: Descr, tab: String):String {
            return tab + "  " + descr.quot + item.first + descr.quot + ": " + item.second.build(descr, tab + "  ")
        }

        fun buildRoot(descr: Descr): String {
            val start = if (descr.open.isEmpty()) "" else (descr.open + "\n")
            val end = descr.close
            val tab = if (descr.rootTab) "  " else ""
            return """$start${list.map {formatItem(it, descr, tab)}.joinToString (descr.sep + "\n")}
                |$tab$end""".trimMargin()
        }
    }

    private fun expectedRu(tg: TranslationGenerator, generatedKey: String): String {
        val content = Obj().with("ref",
            Obj().with("section",
                Obj().with("key", "Перевод").with("missing", generatedKey)
            )
        ).build(Descr("\"", "{", "}", ","), "")
        if (tg.ext()=="yml") {
            return """ref:
                |  section:
                |    key: Перевод
                |    missing: $generatedKey
            """.trimMargin()
        } else {
            return content
        }
    }

    private fun contentRu(tg: TranslationGenerator): String {
        val content = Obj().with("ref",
            Obj().with("section",
                Obj().with("key", "Перевод")
            )
        ).build(Descr("\"", "{", "}", ","), "")
        if (tg.ext()=="yml") {
            return """ref:
                |  section:
                |    key: Перевод
            """.trimMargin()
        } else {
            return content
        }
    }

    private fun expectedEn(tg: TranslationGenerator, generatedKey: String): String {
        val content = Obj().with("ref",
            Obj().with("section",
                Obj().with("key", "Translation").with("missing", generatedKey)
            )
        ).build(Descr("\"", "{", "}", ","), "")
        if (tg.ext()=="yml") {
            return """ref:
                |  section:
                |    key: Translation
                |    missing: $generatedKey""".trimMargin()
        } else {
            return content
        }
    }

    private fun contentEn(tg: TranslationGenerator): String {
        val content = Obj().with("ref",
            Obj().with("section",
                Obj().with("key", "Translation")
            )
        ).build(Descr("\"", "{", "}", ","), "")
        if (tg.ext()=="yml") {
            return """ref:
                |  section:
                |    key: Translation""".trimMargin()
        } else {
            return content
        }
    }
}