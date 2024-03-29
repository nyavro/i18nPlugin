package com.eny.i18n.extensions.lang.js

import com.eny.i18n.plugin.factory.TranslationExtractor
import com.eny.i18n.plugin.utils.toBoolean
import com.intellij.lang.ecmascript6.JSXHarmonyFileType
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.lang.javascript.TypeScriptJSXFileType
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag

internal class JsxTranslationExtractor: TranslationExtractor {
    override fun canExtract(element: PsiElement): Boolean =
        listOf(JSXHarmonyFileType.INSTANCE, TypeScriptJSXFileType.INSTANCE).any { it == element.containingFile.fileType } &&
            PsiTreeUtil.getParentOfType(element, XmlTag::class.java)?.let {
                !PsiTreeUtil.findChildOfType(it, XmlTag::class.java).toBoolean()
            } ?: false

    override fun isExtracted(element: PsiElement): Boolean =
        element.isJs() && JSPatterns.jsArgument("t", 0).accepts(element.parent)

    override fun text(element: PsiElement): String =
        if (element.parent is XmlAttributeValue) element.text
        else PsiTreeUtil.getParentOfType(element, XmlTag::class.java)!!
            .value
            .textElements
            .map {it.text}
            .joinToString(" ")

    override fun textRange(element: PsiElement): TextRange =
        if (element.parent is XmlAttributeValue) element.parent.textRange
        else PsiTreeUtil.getParentOfType(element, XmlTag::class.java)!!
            .value
            .textElements
            .let {
                TextRange(
                        it.first().textRange.startOffset,
                        it.last().textRange.endOffset
                )
            }

    override fun template(element: PsiElement): (argument: String) -> String = {
        "{i18n.t($it)}"
    }
    private fun PsiElement.isJs(): Boolean = this.language == JavascriptLanguage.INSTANCE
}