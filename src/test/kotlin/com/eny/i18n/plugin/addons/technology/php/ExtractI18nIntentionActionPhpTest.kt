package com.eny.i18n.plugin.addons.technology.php

import com.eny.i18n.plugin.ide.actions.ExtractI18nIntentionActionTest
import com.eny.i18n.plugin.ide.actions.noop
import com.eny.i18n.plugin.utils.generator.code.PhpDoubleQuoteCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.PhpSingleQuoteCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator

class ExtractI18nIntentionActionPhpTest : ExtractI18nIntentionActionTest(PhpDoubleQuoteCodeGenerator(), JsonTranslationGenerator(), ::noop)
class ExtractI18nIntentionActionPhpSingleTest : ExtractI18nIntentionActionTest(PhpSingleQuoteCodeGenerator(), JsonTranslationGenerator(), ::noop)