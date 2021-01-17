package com.eny.i18n.plugin.yaml

import com.eny.i18n.plugin.addons.yaml.YamlSettings
import com.eny.i18n.plugin.ide.actions.ExtractI18nIntentionActionTest
import com.eny.i18n.plugin.utils.generator.code.JsCodeGenerator

class ExtractI18nIntentionActionYamlTest: ExtractI18nIntentionActionTest(
    JsCodeGenerator(),
        YamlTranslationGenerator(),
        {
            fixture, block ->
                fixture.runYamlConfig(Pair(YamlSettings::preferYamlFilesGeneration, true)) {
                    block()
                }
        }
)