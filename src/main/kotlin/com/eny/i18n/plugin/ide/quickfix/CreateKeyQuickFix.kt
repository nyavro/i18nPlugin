package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.ContentGenerator
import com.eny.i18n.LocalizationSource
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.utils.LocalizationSourceService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

/**
 * Quick fix for missing key creation
 */
class CreateKeyQuickFix(
    private val fullKey: FullKey,
    private val selector: SourcesSelector,
    private val commandCaption: String,
        //TODO: request translation value after invoking 'Create i18n key' quickfix
    private val translationValue: String? = null,
    private val onComplete: () -> Unit = {}): QuickFix(), CompositeKeyResolver<PsiElement> {

    override fun getText(): String = commandCaption

    override fun invoke(project: Project, editor: Editor) =
        ApplicationManager.getApplication().invokeLater {
            val sourceService = project.service<LocalizationSourceService>()
            val jsonFiles = sourceService.findSources(fullKey.allNamespaces(), project)
            if (jsonFiles.size == 1) {
                createPropertyInFile(project, jsonFiles.first())
            } else if (jsonFiles.size > 1) {
                createPropertyInFiles(project, editor, jsonFiles)
            }
        }

    private fun createPropertyInFiles(project: Project, editor: Editor, sources: List<LocalizationSource>) =
        selector.select(sources, {it.forEach {createPropertyInFile(project, it)}}, editor)

    private fun createPropertyInFile(project: Project, target: LocalizationSource) {
        val ref = resolveCompositeKey(
            fullKey.compositeKey,
            target
        )
        if (ref.element != null) {
            CommandProcessor.getInstance().executeCommand(
                project,
                {
                    ApplicationManager.getApplication().runWriteAction {
                        createPropertiesChain(ref.element.value(), ref.unresolved, target.localization.contentGenerator())
                        onComplete()
                    }
                },
                commandCaption,
                UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION
            )
        }
    }

    private fun createPropertiesChain(element: PsiElement, unresolved: List<Literal>, generator: ContentGenerator) {
        if(generator.isSuitable(element)) {
            generator.generate(element, fullKey, unresolved, translationValue)
        }
    }
}