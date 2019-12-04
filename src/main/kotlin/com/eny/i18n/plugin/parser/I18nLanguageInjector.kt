package com.eny.i18n.plugin.parser

import com.intellij.openapi.util.TextRange
import com.intellij.psi.InjectedLanguagePlaces
import com.intellij.psi.LanguageInjector
import com.intellij.psi.PsiLanguageInjectionHost

class I18nLanguageInjector: LanguageInjector {

    override fun getLanguagesToInject(host: PsiLanguageInjectionHost, injectionPlacesRegistrar: InjectedLanguagePlaces) {
        if (host.node.elementType.toString() == "JS:LITERAL_EXPRESSION") {
            injectionPlacesRegistrar.addPlace(
                I18nLanguage.instance, TextRange(1, host.textRange.endOffset - host.textRange.startOffset - 1), null, null
            )
        } else {
            val v = host.text
            println(v)
        }
    }
}