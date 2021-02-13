package com.eny.i18n.plugin.yaml

import com.eny.i18n.plugin.addons.yaml.YamlSettings
import com.eny.i18n.plugin.ide.actions.TranslationFileGenerationTestBase
import com.eny.i18n.plugin.ide.actions.noop
import com.eny.i18n.plugin.utils.generator.code.JsCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.PhpDoubleQuoteCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.PhpSingleQuoteCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator

class TranslationFileGenerationTest : TranslationFileGenerationTestBase(
    JsCodeGenerator(),
    YamlTranslationGenerator(),
    preferYamlGeneration()
)
