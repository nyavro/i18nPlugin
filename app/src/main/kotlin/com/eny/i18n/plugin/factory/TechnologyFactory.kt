package com.eny.i18n.plugin.factory

import com.eny.i18n.plugin.utils.LocalizationSource
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

interface LocalizationSourcesProvider {
    fun find(fileNames: List<String>, element: PsiElement?, isHost: Boolean, project: Project): List<LocalizationSource>
}

/**
 * Translation technology interfaces factory
 */
interface TechnologyFactory {
    fun localizationSourcesProvider(): LocalizationSourcesProvider
}