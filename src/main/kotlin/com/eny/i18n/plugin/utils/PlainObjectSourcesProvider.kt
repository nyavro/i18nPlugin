package com.eny.i18n.plugin.utils

import com.eny.i18n.LocalizationSource
import com.eny.i18n.LocalizationSourceProvider
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex

class PlainObjectSourcesProvider: LocalizationSourceProvider {

    override fun findLocalizationSources(project: Project, fileNames: List<String>): List<LocalizationSource> {
        return findVirtualFilesUnder(project, "LC_MESSAGES")
            .filter { it.virtualFile.extension == "po" }
            .map {
                localizationSource(it, { file: PsiFile -> file.containingDirectory.parentDirectory ?: file.containingDirectory })
            }
    }

    private fun localizationSource(file: PsiFile, resolveParent: (file: PsiFile) -> PsiDirectory): LocalizationSource {
        val parentDirectory = resolveParent(file)
        return LocalizationSource(
            file,
            file.name,
            parentDirectory.name,
            pathToRoot(
                file.project.basePath ?: "",
                file.containingDirectory
                    .virtualFile
                    .path
            ).trim('/') + '/' + file.name,
            file.fileType
        )
    }

    private fun findVirtualFilesUnder(project: Project, directory: String): List<PsiFile> =
        FilenameIndex.getFilesByName(project, directory, Settings.getInstance(project).config().searchScope(project), true).toList().flatMap {
            it.children.toList().mapNotNull { root -> root.containingFile}
        }
}