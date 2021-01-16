package com.eny.i18n.plugin.yaml

import com.eny.i18n.plugin.ide.completion.CodeCompletionDefNsTestBase
import com.eny.i18n.plugin.ide.completion.CodeCompletionTestBase
import com.eny.i18n.plugin.ide.completion.NsKeyGenerator
import com.eny.i18n.plugin.utils.generator.code.TsCodeGenerator

internal class CodeCompletionTsYamlDefNsTest: CodeCompletionDefNsTestBase(TsCodeGenerator(), YamlTranslationGenerator())
internal class CodeCompletionTsYamlTest: CodeCompletionTestBase(TsCodeGenerator(), YamlTranslationGenerator(), NsKeyGenerator())