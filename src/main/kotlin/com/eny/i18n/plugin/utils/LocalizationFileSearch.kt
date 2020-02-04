package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.json.JsonFileType
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.yaml.YAMLFileType

/**
 * Provides search of localization files (json, yaml)
 */
class LocalizationFileSearch(private val project: Project) {

    val settings = Settings.getInstance(project)

    /**
     * Finds json roots by json file name
     */
    fun findFilesByName(fileName: String?): List<PsiFile> =
        findVirtualFilesByName(fileName ?: settings.defaultNs).flatMap {vf -> listOfNotNull(findPsiRoot(vf))} +
            if (settings.vue) findVirtualFilesUnder(settings.vueDirectory)
            else listOf()

    private fun findVirtualFilesUnder(directory: String): List<PsiFile> =
        FilenameIndex.getFilesByName(project, directory, settings.searchScope(project), true).toList().flatMap {
            item -> item.children.toList().map {root -> root.containingFile}
        }

    /**
     * Finds virtual files by file name
     */
    private fun findVirtualFilesByName(fileName: String) =
        findVirtualFilesByName(fileName, JsonFileType.INSTANCE) + findVirtualFilesByName(fileName, YAMLFileType.YML)

    /**
     * Finds virtual files by file name and type
     */
    private fun findVirtualFilesByName(fileName: String, fileType: FileType): List<VirtualFile> {
        val ext = fileType.defaultExtension
        return FileTypeIndex
            .getFiles(
                fileType,
                if (settings.searchInProjectOnly) GlobalSearchScope.projectScope(project)
                else GlobalSearchScope.allScope(project)
            )
            .filter { file -> file.name == "$fileName.$ext" }
    }
    /**
     * Finds root of virtual file
     */
    private fun findPsiRoot(virtualFile: VirtualFile):PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
}