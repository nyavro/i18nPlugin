package com.eny.i18n.plugin.addons.technology.i18n

import com.eny.i18n.plugin.ide.annotator.LocalizationSourcesProvider
import com.eny.i18n.plugin.ide.annotator.i18NextSettings
import com.eny.i18n.plugin.ide.annotator.LocalizationSource
import com.eny.i18n.plugin.ide.annotator.SearchUtility
import com.eny.i18n.plugin.utils.whenMatches
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class I18nLocalizationSources : LocalizationSourcesProvider {

    override fun find(fileNames: List<String>, element: PsiElement?, isHost: Boolean, project: Project): List<LocalizationSource> {
        val searchUtil = SearchUtility(project)
        return searchUtil.findSourcesByNames(
            fileNames.whenMatches { it.isNotEmpty() } ?: project.i18NextSettings().defaultNamespaces()
        )
    }
}