package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.ide.Settings
import com.intellij.json.JsonFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope

class JsonSearchUtil(private val project: Project) {

    val settings = Settings.getInstance(project)

    /**
     * Finds json roots by json file name
     */
    fun findFilesByName(fileName: String): List<PsiFile> {
        val value = findVirtualFilesByName(fileName)
        return value.flatMap {vf -> listOfNotNull(findPsiRoot(vf))}
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