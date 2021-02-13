package com.eny.i18n.plugin.addons.technology.php

import com.eny.i18n.plugin.ide.actions.KeyContextDefaultNsTestBase
import com.eny.i18n.plugin.ide.actions.KeyContextTestBase
import com.eny.i18n.plugin.utils.generator.code.PhpDoubleQuoteCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.PhpSingleQuoteCodeGenerator

class KeyContextPhpDs : KeyContextDefaultNsTestBase(PhpSingleQuoteCodeGenerator())
class KeyContextPhp : KeyContextTestBase(PhpSingleQuoteCodeGenerator())
class KeyContextPhp2Ds : KeyContextDefaultNsTestBase(PhpDoubleQuoteCodeGenerator())
class KeyContextPhp2 : KeyContextTestBase(PhpDoubleQuoteCodeGenerator())