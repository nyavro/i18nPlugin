package com.eny.i18n.plugin.language

import com.intellij.lexer.FlexAdapter

class I18nLexerAdapter : FlexAdapter(I18nLexer(null))