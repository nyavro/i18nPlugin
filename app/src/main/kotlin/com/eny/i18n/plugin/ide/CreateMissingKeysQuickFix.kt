package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.key.CompositeKeyResolver
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.PropertyReference
import com.eny.i18n.plugin.utils.whenMatches
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
        private val references: List<PropertyReference<PsiElement, LocalizationType>>,
        private val commandCaption: String): QuickFix(), CompositeKeyResolver<PsiElement, LocalizationType> {

    override fun getText(): String = commandCaption

    override fun invoke(project: Project, editor: Editor) =
        ApplicationManager.getApplication().invokeLater {
            references.map { createPropertyInFile(project, it) }
        }

    private fun createPropertyInFile(project: Project, ref: PropertyReference<PsiElement, LocalizationType>) {
        ref.element?.whenMatches {!it.isLeaf()}?.let {
            CommandProcessor.getInstance().executeCommand(
                project,
                {ApplicationManager.getApplication().runWriteAction {
                    mainFactory
                            .contentGenerator(ref.type)
                            ?.generate(it.value(), fullKey, ref.unresolved, fullKey.source) }
                },
                commandCaption,
                UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION
            )
        }
    }
}