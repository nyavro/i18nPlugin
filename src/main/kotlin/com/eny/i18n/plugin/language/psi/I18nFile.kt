package com.eny.i18n.plugin.language.psi

import com.eny.i18n.plugin.language.I18nFileType
import com.eny.i18n.plugin.language.I18nLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class I18nFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, I18nLanguage.Instance) {
    override fun getFileType(): FileType = I18nFileType.Instance
    override fun toString() = "I18n file"
}