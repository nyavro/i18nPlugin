package com.eny.i18n.plugin.language

import com.intellij.lang.Language

class I18nLanguage private constructor(): Language("I18n") {
    companion object {
        val Instance: I18nLanguage = I18nLanguage()
    }
}