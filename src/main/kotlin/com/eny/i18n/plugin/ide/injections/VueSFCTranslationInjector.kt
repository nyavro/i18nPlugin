package com.eny.i18n.plugin.ide.injections

import com.intellij.json.JsonLanguage
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.InjectedLanguagePlaces
import com.intellij.psi.LanguageInjector
import com.intellij.psi.PsiLanguageInjectionHost

/**
 * Json language injector for i18n xml element in Vue SFC.
 */
class VueSFCTranslationInjector : LanguageInjector {

    override fun getLanguagesToInject(host: PsiLanguageInjectionHost, places: InjectedLanguagePlaces) {
        if (host.isValidHost && XmlPatterns.xmlText().withParent(XmlPatterns.xmlTag().withName("i18n")).accepts(host)) {
            places.addPlace(JsonLanguage.INSTANCE, TextRange(0, host.textRange.length), null, null)
        }
    }
}
