package com.eny.i18n.plugin.yaml

import com.eny.i18n.plugin.addons.yaml.YamlSettings
import com.eny.i18n.plugin.ide.actions.ExtractI18nIntentionActionTest
import com.eny.i18n.plugin.utils.generator.code.JsCodeGenerator
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

class ExtractI18nIntentionActionYamlTest: ExtractI18nIntentionActionTest(
    JsCodeGenerator(),
    YamlTranslationGenerator(),
    preferYamlGeneration()
)