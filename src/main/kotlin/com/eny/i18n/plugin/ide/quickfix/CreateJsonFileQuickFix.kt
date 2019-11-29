package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.FullKey
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.json.JsonLanguage
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager

class CreateJsonFileQuickFix(private val fullKey: FullKey) : BaseIntentionAction() {

    override fun getFamilyName(): String = "i18n plugin"

    override fun getText(): String = "Create translation files"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) =
        ApplicationManager.getApplication().invokeLater {
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
                val settings = Settings.getInstance(project)
                val name: String = (fullKey.ns?.text ?: settings.defaultNs) + ".json"
                val content: String = generateContent(fullKey)
                ApplicationManager.getApplication().runWriteAction {
                    folders.forEach { folder ->
                        PsiManager.getInstance(project).findDirectory(folder)?.add(
                                PsiFileFactory.getInstance(project).createFileFromText(name, JsonLanguage.INSTANCE, content)
                        )
                    }
                }
            }
        }

    private fun generateContent(fullKey: FullKey): String =
        fullKey.compositeKey.foldRightIndexed("\"TODO-${fullKey.source}\"", {
            i, key, acc ->
                val tab = "\t".repeat(i)
                "{\n\t$tab\"${key.text}\": $acc\n$tab}"
        })
}
