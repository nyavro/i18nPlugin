package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.JsonSearch
import com.eny.i18n.plugin.utils.Literal
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.json.psi.JsonElementGenerator
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonPsiUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class CreatePropertyQuickFix(
        private val fullKey: FullKey,
        private val selector: FilesSelector,
        private val commandCaption: String): BaseIntentionAction(), CompositeKeyResolver<PsiElement> {

    override fun getFamilyName(): String = "i18n quick fix"

    override fun getText(): String = commandCaption

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        ApplicationManager.getApplication().invokeLater {
            if (editor != null) {
                val jsonFiles = JsonSearch(project).findFilesByName(fullKey.ns?.text)
                if (jsonFiles.size == 1) {
                    createPropertyInFile(project, jsonFiles.first())
                } else if (jsonFiles.size > 1) {
                    createPropertyInFiles(project, editor, jsonFiles)
                }
            }
        }
    }

    private fun createPropertyInFiles(project: Project, editor: Editor, jsonFiles: List<PsiFile>) =
        selector.select(jsonFiles, {file: PsiFile -> createPropertyInFile(project, file)}, editor)

    private fun createPropertyInFile(project: Project, target: PsiFile) {
        val ref = resolveCompositeKey(
            fullKey.compositeKey,
            PsiElementTree.create(target)
        )
        if (ref.element != null) {
            CommandProcessor.getInstance().executeCommand(
                project,
                { createPropertiesChain(project, ref.element.value(), ref.unresolved) },
                commandCaption,
                UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION
            )
        }
    }

    private fun createPropertiesChain(project: Project, element: PsiElement, unresolved: List<Literal>) {
        if (unresolved.isNotEmpty() && element is JsonObject) {
            val first = unresolved.first()
            val text = unresolved.drop(1).foldRight("\"TODO-${fullKey.source}\"") {
                item, acc -> "{\"${item.text}\": $acc}"
            }
            ApplicationManager.getApplication().runWriteAction {
                JsonPsiUtil
                    .addProperty(
                        element,
                        JsonElementGenerator(project).createProperty(first.text, text),
                        false
                    )
            }
        }
    }
}