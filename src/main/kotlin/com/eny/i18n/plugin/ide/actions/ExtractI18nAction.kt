package com.eny.i18n.plugin.ide.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile


/**
 * Action extracting i18n from selected string
 */
class ExtractI18nAction: AnAction() {

    private fun isValidKey() = object : InputValidator {
        override fun checkInput(inputString: String?): Boolean {
            return (inputString ?: "").isNotEmpty()
        }

        override fun canClose(inputString: String?): Boolean = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getRequiredData(CommonDataKeys.PROJECT)
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val document = editor.document
        val primaryCaret = editor.caretModel.primaryCaret
        val start = primaryCaret.selectionStart
        val end = primaryCaret.selectionEnd
        val key = Messages.showInputDialog(project, "Specify the key", "Input i18n key", Messages.getQuestionIcon(), null, isValidKey())
        val template = "i18n.t"
        val isWide = isQuoted(document.getText(TextRange(start-1, end+1)))
        val wideCorrection = if (isWide) 1 else 0
        WriteCommandAction.runWriteCommandAction(project) {
            document.replaceString(start - wideCorrection, end + wideCorrection, "$template('$key')")
        }
        primaryCaret.removeSelection()
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val document = editor.document
        val primaryCaret = editor.caretModel.primaryCaret
        val start = primaryCaret.selectionStart
        val end = primaryCaret.selectionEnd
        val widenText = document.getText(TextRange(start-1, end+1))
        e.presentation.isEnabled =
            isQuoted(widenText) ||
            primaryCaret.selectedText?.let {
                text -> isQuoted(text) && (e.getData(LangDataKeys.PSI_FILE)?.let { psiFile -> isJsSource(psiFile) } ?: false )
            } ?: false
    }

    private fun isJsSource(psiFile: PsiFile) = listOf("TypeScript", "JavaScript").contains(psiFile.fileType.name)

    private fun isQuoted(text: String): Boolean =
        (text.length > 1) && listOf("\"", "'", "`").any {quote -> text.startsWith(quote) && text.endsWith(quote)}
}