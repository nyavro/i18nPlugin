package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.factory.MainFactory
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PropertyReference
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

/**
 * Quick fix for missing keys creation
 */
class CreateMissingKeysQuickFix(
        private val fullKey: FullKey,
        private val mainFactory: MainFactory,
        private val references: List<PropertyReference<PsiElement>>,
        private val commandCaption: String): QuickFix(), CompositeKeyResolver<PsiElement> {

    override fun getText(): String = commandCaption

    override fun invoke(project: Project, editor: Editor) =
        ApplicationManager.getApplication().invokeLater {
            createPropertyInFiles(project)
        }

    private fun createPropertyInFiles(project: Project) =
        references.map {
            createPropertyInFile(project, it)
        }

    private fun createPropertyInFile(project: Project, ref: PropertyReference<PsiElement>) {
        if (ref.element != null && !ref.element.isLeaf()) {
            CommandProcessor.getInstance().executeCommand(
                project,
                {ApplicationManager.getApplication().runWriteAction {
                    mainFactory
                        .contentGenerator(ref.type)
                        ?.generate(ref.element.value(), fullKey, ref.unresolved, fullKey.source) }
                },
                commandCaption,
                UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION
            )
        }
    }
}