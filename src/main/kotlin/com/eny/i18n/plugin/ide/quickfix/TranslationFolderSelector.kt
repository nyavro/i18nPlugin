package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.searchScope
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiFileSystemItem
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex

/**
 * Represents folder selection
 */
interface TranslationFolderSelector {

    /**
     * Selects folders
     */
    fun select(callback: (List<PsiFileSystemItem>) -> Unit)
}

/**
 * Vue translation folder selector
 */
class Vue18nTranslationFolderSelector(val project: Project) : TranslationFolderSelector {
    private val settings = Settings.getInstance(project)
    override fun select(callback: (List<PsiFileSystemItem>) -> Unit) {
        callback(
            FilenameIndex.getFilesByName(project, settings.vueDirectory, settings.searchScope(project), true)
                .asList()
        )
    }
}

/**
 * Vue translation folder selector
 */
class I18NextTranslationFolderSelector(val project: Project) : TranslationFolderSelector {
    override fun select(callback: (List<PsiFileSystemItem>) -> Unit) {
        val descriptor = FileChooserDescriptorFactory
                .createMultipleFoldersDescriptor()
                .withDescription("Select destination folder/folders")
                .withShowHiddenFiles(false)
        descriptor.roots = listOf(project.guessProjectDir())
        FileChooser.chooseFiles(
                descriptor,
                project,
                null
        ) {
            files -> callback(files.mapNotNull {folder -> PsiManager.getInstance(project).findDirectory(folder)})
        }
    }

}
