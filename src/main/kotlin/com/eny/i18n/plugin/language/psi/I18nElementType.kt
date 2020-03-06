package com.eny.i18n.plugin.language.psi

import com.eny.i18n.plugin.language.I18nLanguage
import com.intellij.psi.tree.IElementType

class I18nElementType(debugName: String) : IElementType(debugName, I18nLanguage.Instance)