package com.eny.i18n.plugin.addons.technology.po

import com.eny.i18n.plugin.ide.*
import com.eny.i18n.plugin.ide.annotator.*
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class PlainObjectLocalizationSources : LocalizationSourcesProvider {

    override fun find(fileNames: List<String>, element: PsiElement?, isHost: Boolean, project: Project): List<LocalizationSource> {
        val searchUtil = SearchUtility(project)
        return searchUtil.findVirtualFilesUnder("LC_MESSAGES")
            .filter { it.virtualFile.extension == "po" }
            .map {
                localizationSource(it, { file: PsiFile ->
                    file.containingDirectory.parentDirectory ?: file.containingDirectory
                }, LocalizationType(listOf(it.fileType), "general"))
            }
    }
}