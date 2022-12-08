package com.eny.i18n

import com.intellij.openapi.fileTypes.FileType

interface Technology {
    fun fileTypes(): List<FileType>
}