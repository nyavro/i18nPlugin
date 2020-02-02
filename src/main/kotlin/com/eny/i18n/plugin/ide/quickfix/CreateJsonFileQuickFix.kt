package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.searchScope
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex

/**
 * Quick fix for json file creation
 */
class CreateJsonFileQuickFix(
        private val fullKey: FullKey,
        private val isVueContext: Boolean = false,
        private val contentGenerator: ContentGenerator) : BaseIntentionAction() {

    override fun getFamilyName(): String = "i18n plugin"

    override fun getText(): String = "Create translation files"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) =
        ApplicationManager.getApplication().invokeLater {
            val settings = Settings.getInstance(project)
            if (isVueContext) {
                val folders = FilenameIndex.getFilesByName(project, settings.vueDirectory, settings.searchScope(project), true)
                val name = "en." + contentGenerator.getFileType().defaultExtension
                val content: String = contentGenerator.generateContent(fullKey)
                ApplicationManager.getApplication().runWriteAction {
                    folders.forEach { folder ->
                        folder.add(
                            PsiFileFactory.getInstance(project).createFileFromText(name, contentGenerator.getLanguage(), content)
                        )
                    }
                }
            } else {
                val descriptor = FileChooserDescriptorFactory
                        .createMultipleFoldersDescriptor()
                        .withDescription("Select destination folder/folders")
                        .withShowHiddenFiles(false)
                descriptor.roots = listOf(project.guessProjectDir())
                FileChooser.chooseFiles(
                        descriptor,
                        project,
                        null
                ) { folders ->
                    val name: String = (fullKey.ns?.text ?: settings.defaultNs) + contentGenerator.getFileType().defaultExtension
                    val content: String = contentGenerator.generateContent(fullKey)
                    ApplicationManager.getApplication().runWriteAction {
                        folders.forEach { folder ->
                            PsiManager.getInstance(project).findDirectory(folder)?.add(
                                PsiFileFactory.getInstance(project).createFileFromText(name, contentGenerator.getLanguage(), content)
                            )
                        }
                    }
                }
            }
        }
}
