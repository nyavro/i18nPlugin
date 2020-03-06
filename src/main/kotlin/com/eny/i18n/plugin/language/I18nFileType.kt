package com.eny.i18n.plugin.language

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

/**
 * I18n experimental file
 */
class I18nFileType private constructor () : LanguageFileType(I18nLanguage.Instance) {

    companion object {
        val Instance = I18nFileType()
    }
    override fun getIcon(): Icon? = I18nIcon.Icon

    override fun getName(): String = "I18n file"

    override fun getDefaultExtension(): String = "i18n"

    override fun getDescription(): String = "i18n experimental file"
}