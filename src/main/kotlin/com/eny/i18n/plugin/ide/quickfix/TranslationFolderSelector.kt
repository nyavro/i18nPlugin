package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.PluginBundle
import com.eny.i18n.plugin.utils.nullableToList
import com.eny.i18n.plugin.utils.searchScope
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
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
            FilenameIndex
                .getFilesByName(project, settings.vueDirectory, settings.searchScope(project), true)
                .asList()
        )
    }
}

/**
 * Vue translation folder selector
 */
class I18NextTranslationFolderSelector(val project: Project) : TranslationFolderSelector {

    override fun select(callback: (List<PsiFileSystemItem>) -> Unit) {
        val projectDir = project.guessProjectDir()
        val processFiles: (files: List<VirtualFile>) -> Unit = {
            files -> callback(files.mapNotNull{PsiManager.getInstance(project).findDirectory(it)})
        }
        if (ApplicationManager.getApplication().isHeadlessEnvironment) {
            // Hate this hack (because it is solely for testing purposes), but there is no way to substitute FileChooser from tests
            // like it is done by Messages.setTestInputDialog(inputDialog)
            processFiles(projectDir.nullableToList())
        } else {
            FileChooser.chooseFiles(
                FileChooserDescriptorFactory
                    .createMultipleFoldersDescriptor()
                    .withDescription(PluginBundle.getMessage("quickfix.choose.files"))
                    .withShowHiddenFiles(false)
                    .withRoots(projectDir),
                project,
                projectDir,
                processFiles
            )
        }
    }
}
