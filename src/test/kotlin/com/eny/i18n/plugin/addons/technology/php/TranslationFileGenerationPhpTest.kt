package com.eny.i18n.plugin.addons.technology.php

import com.eny.i18n.plugin.ide.actions.TranslationFileGenerationTestBase
import com.eny.i18n.plugin.ide.actions.noop
import com.eny.i18n.plugin.utils.generator.code.PhpDoubleQuoteCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.PhpSingleQuoteCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator

class TranslationFileGenerationPhpTest : TranslationFileGenerationTestBase(PhpSingleQuoteCodeGenerator(), JsonTranslationGenerator(), ::noop)
class TranslationFileGenerationPhp2Test : TranslationFileGenerationTestBase(PhpDoubleQuoteCodeGenerator(), JsonTranslationGenerator(), ::noop)
