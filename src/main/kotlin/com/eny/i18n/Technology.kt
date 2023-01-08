package com.eny.i18n

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

interface Technology {
    fun translationFunctionNames(): List<String>
    fun findSourcesByConfiguration(project: Project): List<LocalizationSource>
    fun initialize(project: Project)
    fun cfgNamespaces(): List<String>
}
