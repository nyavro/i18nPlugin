package com.eny.i18n.extensions.lang.js

import com.eny.i18n.Lang
import com.eny.i18n.plugin.factory.CallContext
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.utils.type
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.ecmascript6.psi.ES6Property
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSProperty
import com.intellij.lang.javascript.psi.ecma6.TypeScriptEnumField
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parents

class JsxLang : JsLang() {

    override fun canExtractKey(element: PsiElement): Boolean = false
}
