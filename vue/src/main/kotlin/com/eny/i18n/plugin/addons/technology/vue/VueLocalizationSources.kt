package com.eny.i18n.plugin.addons.technology.vue

import com.eny.i18n.plugin.factory.LocalizationSourcesProvider
import com.eny.i18n.plugin.ide.settings.mainFactory
import com.eny.i18n.plugin.utils.LocalizationSource
import com.eny.i18n.plugin.utils.SearchUtility
import com.eny.i18n.plugin.utils.directParent
import com.eny.i18n.plugin.utils.localizationSource
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VueLocalizationSources : LocalizationSourcesProvider {

    override fun find(fileNames: List<String>, element: PsiElement?, isHost: Boolean, project: Project): List<LocalizationSource> {
        val searchUtil = SearchUtility(project)
        val mp = project
            .mainFactory()
            .contentGenerators()
            .flatMap {cg -> cg.getType().fileTypes.map{Pair(it, cg.getType())}}
            .toMap()
        return searchUtil
            .findVirtualFilesUnder(project.vueSettings().vueDirectory)
            .mapNotNull {
                psiFile -> mp.get(psiFile.fileType)?.let { localizationSource(psiFile, directParent, it) }
            }
    }
}

