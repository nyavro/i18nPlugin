package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.utils.LocalizationSource
import com.eny.i18n.plugin.utils.LocalizationSourceSearch
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.UndoConfirmationPolicy
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
    private val generators: List<ContentGeneratorAdapter>,
    private val translationValue: String? = null,
    private val onComplete: () -> Unit = {}): QuickFix(), CompositeKeyResolver<PsiElement> {

    override fun getText(): String = commandCaption

    override fun invoke(project: Project, editor: Editor) =
        ApplicationManager.getApplication().invokeLater {
            val jsonFiles = LocalizationSourceSearch(project).findFilesByName(fullKey.ns?.text)
            if (jsonFiles.size == 1) {
                createPropertyInFile(project, jsonFiles.first())
            } else if (jsonFiles.size > 1) {
                createPropertyInFiles(project, editor, jsonFiles)
            }
        }

    private fun createPropertyInFiles(project: Project, editor: Editor, jsonFiles: List<LocalizationSource>) =
        selector.select(jsonFiles, {source: LocalizationSource -> createPropertyInFile(project, source)}, onComplete, editor)

    private fun createPropertyInFile(project: Project, target: LocalizationSource) {
        val ref = resolveCompositeKey(
            fullKey.compositeKey,
            PsiElementTree.create(target.element)
        )
        if (ref.element != null) {
            CommandProcessor.getInstance().executeCommand(
                project,
                {
                    ApplicationManager.getApplication().runWriteAction {
                        createPropertiesChain(ref.element.value(), ref.unresolved)
                        //TODO: onComplete here is in double "transaction". This should be fixed
                        onComplete()
                    }
                },
                commandCaption,
                UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION
            )
        }
    }

    private fun createPropertiesChain(element: PsiElement, unresolved: List<Literal>) =
        generators
            .filter { generator -> generator.isSuitable(element) }
            .forEach { generator -> generator.generate(element, fullKey, unresolved, translationValue) }
}