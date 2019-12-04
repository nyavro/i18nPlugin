package com.eny.i18n.plugin.parser

import com.intellij.lang.Language

class I18nLanguage private constructor() : Language("I18n") {
    companion object {
        val instance = I18nLanguage()
    }
}
