package com.eny.i18n.plugin.addons.technology.vue

import com.eny.i18n.plugin.ide.actions.ExtractionTestBase
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import org.junit.Assert
import org.junit.Ignore
//@TODO disabled test 2
//class ExtractI18nIntentionActionVueTestCase1: ExtractI18nIntentionActionVueTest("<caret>I want to move it to translation")
//class ExtractI18nIntentionActionVueTestCase2: ExtractI18nIntentionActionVueTest("I want to mov<caret>e it to translation")
//class ExtractI18nIntentionActionVueTestCase3: ExtractI18nIntentionActionVueTest("I want to move it to translation<caret>")

abstract class ExtractI18nIntentionActionVueTest(private val text: String) /*ExtractionTestBase()*/ {

//    private val cg = VueCodeGenerator()
//    private val tg = JsonTranslationGenerator()
//
//    fun disabledTestKeyExtractionTemplate() {
//        myFixture.runVueConfig {
//            runTestCase(
//                "App.${cg.ext()}",
//                cg.generateTemplate(text),
//                cg.generateTemplate("{{ \$t('ref.value3') }}"),
//                "locales/en-US.${tg.ext()}",
//                tg.generate("ref", arrayOf("section", "key", "Reference in json")),
//                tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
//                predefinedTextInputDialog("ref.value3")
//            )
//        }
//    }
//
//    fun disabledTestScriptExtraction() {
//        myFixture.runVueConfig {
//            runTestCase(
//                "App.${cg.ext()}",
//                cg.generateScript("\"$text\""),
//                cg.generateScript("this.\$t('ref.value3')"),
//                "locales/en-US.${tg.ext()}",
//                tg.generate("ref", arrayOf("section", "key", "Reference in json")),
//                tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
//                predefinedTextInputDialog("ref.value3")
//            )
//        }
//    }
}