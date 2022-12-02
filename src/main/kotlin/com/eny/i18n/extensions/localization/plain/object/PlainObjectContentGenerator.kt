package com.eny.i18n.extensions.localization.plain.`object`

import com.eny.i18n.ContentGenerator
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiElement

class PlainObjectContentGenerator : ContentGenerator {
    override fun generateContent(compositeKey: List<Literal>, value: String): String {
        TODO("Not yet implemented")
    }

    override fun getType(): FileType {
        TODO("Not yet implemented")
    }

    override fun getLanguage(): Language {
        TODO("Not yet implemented")
    }

    override fun getDescription(): String {
        TODO("Not yet implemented")
    }

    override fun isSuitable(element: PsiElement): Boolean {
        return false
    }

    override fun generateTranslationEntry(item: PsiElement, key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun generate(element: PsiElement, fullKey: FullKey, unresolved: List<Literal>, translationValue: String?) {
        TODO("Not yet implemented")
    }

}
