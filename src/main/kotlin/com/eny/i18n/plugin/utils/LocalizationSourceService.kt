package com.eny.i18n.plugin.utils

import com.eny.i18n.LocalizationSource
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

@Service
class LocalizationSourceService {

    fun findSources(allNamespaces: List<String>, project: Project): List<LocalizationSource> {
        return LocalizationSourceSearch(project).findSources(allNamespaces)
    }

    fun findSources(allNamespaces: List<String>, element: PsiElement): List<LocalizationSource> {
        return LocalizationSourceSearch(element.project).findSources(allNamespaces, element)
    }

    fun findFilesByHost(allNamespaces: List<String>, container: PsiElement): List<LocalizationSource> {
        return LocalizationSourceSearch(container.project).findFilesByHost(allNamespaces, container)
    }
}