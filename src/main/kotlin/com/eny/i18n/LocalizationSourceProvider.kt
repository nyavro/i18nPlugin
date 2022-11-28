package com.eny.i18n

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.openapi.fileTypes.FileType

/**
 * Describes localization source.
 * May be root of json, yaml file, js object
 */
data class LocalizationSource(
    val element: PsiElement, val name: String, val parent: String, val displayPath: String, val type: FileType,
    val host: PsiElement? = null
)

interface LocalizationSourceProvider {
    fun findLocalizationSources(project: Project, fileNames: List<String>): List<LocalizationSource>
}