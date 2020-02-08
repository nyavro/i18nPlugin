package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.utils.FullKey
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory

/**
 * Quick fix for translation file creation
 */
class CreateTranslationFileQuickFix(
        private val fullKey: FullKey,
        private val contentGenerator: ContentGenerator,
        private val folderSelector: TranslationFolderSelector,
        private val fileName: String,
        private val translationValue: String? = null): QuickFix() {

    override fun getText(): String = contentGenerator.getDescription()

    override fun invoke(project: Project, editor: Editor) =
        ApplicationManager.getApplication().invokeLater {
            val name: String = fileName + "." + contentGenerator.getFileType().defaultExtension
            val content: String = contentGenerator.generateContent(fullKey, translationValue)
            folderSelector.select { folders ->
                ApplicationManager.getApplication().runWriteAction {
                    folders.forEach { folder ->
                        folder.add(
                            PsiFileFactory.getInstance(project).createFileFromText(name, contentGenerator.getLanguage(), content)
                        )
                    }
                }
            }
        }
}
