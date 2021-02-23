package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.utils.nullableToList
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFileSystemItem
import com.intellij.psi.PsiManager

/**
 * Vue translation folder selector
 */
class DefaultTranslationFolderSelector : TranslationFolderSelector {

    override fun select(project: Project, callback: (List<PsiFileSystemItem>) -> Unit) {
        val projectDir = project.guessProjectDir()
        val processFiles: (files: List<VirtualFile>) -> Unit = {
            files -> callback(files.mapNotNull{ PsiManager.getInstance(project).findDirectory(it)})
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
