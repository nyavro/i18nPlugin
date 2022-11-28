package com.eny.i18n

import com.intellij.openapi.fileTypes.LanguageFileType

data class FileType(val languageFileType: LanguageFileType, val auxExtensions: List<String> = listOf())

interface Localization {
    fun types(): List<FileType>
}