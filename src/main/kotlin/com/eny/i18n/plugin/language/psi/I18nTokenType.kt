package com.eny.i18n.plugin.language.psi

import com.eny.i18n.plugin.language.I18nLanguage
import com.intellij.psi.tree.IElementType

class I18nTokenType(debugName: String) : IElementType(debugName, I18nLanguage.Instance) {
    override fun toString(): String = "I18nTokenType." + super.toString()
}