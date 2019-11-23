package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.json.JsonFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

class JsonSearchUtil(private val project: Project) {

    val settings = Settings.getInstance(project)

    /**
     * Finds json roots by json file name
     */
    fun findFilesByName(fileName: String?): List<PsiFile> =
        if (settings.vue) findVirtualFilesUnder(settings.vueDirectory)
        else findVirtualFilesByName(fileName ?: settings.defaultNs).flatMap {vf -> listOfNotNull(findPsiRoot(vf))}

    private fun findVirtualFilesUnder(directory: String): List<PsiFile> =
        FilenameIndex.getFilesByName(project, directory, settings.searchScope(project), true).toList().flatMap {
            item -> item.children.toList().map {root -> root.containingFile}
        }

    /**
     * Finds json virtual files by file name
     */
    private fun findVirtualFilesByName(fileName: String) =
        FileTypeIndex
            .getFiles(
                JsonFileType.INSTANCE,
                if (settings.searchInProjectOnly) GlobalSearchScope.projectScope(project)
                else GlobalSearchScope.allScope(project)
            )
            .filter {file -> file.name == "$fileName.json"}

    /**
     * Finds root of virtual file
     */
    private fun findPsiRoot(virtualFile: VirtualFile):PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
}