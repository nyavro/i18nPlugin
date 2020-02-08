package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.quickfix.*
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.*
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

/**
 * Action extracting i18n from selected string
 */
class ExtractI18nAction: AnAction() {

    private val parser = ExpressionKeyParser()

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
        val selectedText = primaryCaret.selectedText ?: return
        val start = primaryCaret.selectionStart
        val end = primaryCaret.selectionEnd
        val key = Messages.showInputDialog(project, "Specify the key", "Input i18n key", Messages.getQuestionIcon(), null, isValidKey()) ?: return
        val i18nKey = parser.parse(listOf(KeyElement.literal(key)))
        if (i18nKey == null) {
            Messages.showErrorDialog("Invalid i18n key", "Key extraction aborted")
            return
        }
        val template = "i18n.t"
        val isWide = document.getText(TextRange(start-1, end+1)).isQuoted()
        val wideCorrection = if (isWide) 1 else 0
        WriteCommandAction.runWriteCommandAction(project) {
            tryToResolveTranslationFile(project, i18nKey, if (isWide) selectedText.unQuote() else selectedText, editor)
            document.replaceString(start - wideCorrection, end + wideCorrection, "$template('$key')")
        }
        primaryCaret.removeSelection()
    }

    private fun tryToResolveTranslationFile(project: Project, i18nKey: FullKey, source: String, editor: Editor) {
        val search = LocalizationFileSearch(project)
        val settings = Settings.getInstance(project)
        val files = search.findFilesByName(i18nKey.ns?.text)
        val preferYaml = false
        val isVue = false
        val quickFix = if (files.isEmpty()) {
            val contentGenerator = if (preferYaml) YamlContentGenerator() else JsonContentGenerator()
            val folderSelector = if (isVue) Vue18nTranslationFolderSelector(project) else I18NextTranslationFolderSelector(project)
            val fileName = if (isVue) "en" else (i18nKey.ns?.text ?: settings.defaultNs)
            CreateTranslationFileQuickFix(i18nKey, contentGenerator, folderSelector, fileName, source)
        } else {
            val generators = listOf(
                ContentGeneratorAdapter(YamlContentGenerator(), YamlPsiContentGenerator()),
                ContentGeneratorAdapter(JsonContentGenerator(), JsonPsiContentGenerator())
            )
            CreatePropertyQuickFix(i18nKey, UserChoice(), "Create i18n property", generators, source)
        }
        quickFix.invoke(project, editor)
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val document = editor.document
        val primaryCaret = editor.caretModel.primaryCaret
        val start = primaryCaret.selectionStart
        val end = primaryCaret.selectionEnd
        val widenText = document.getText(TextRange(start-1, end+1))
        e.presentation.isEnabled =
            widenText.isQuoted() ||
            primaryCaret.selectedText?.let {
                text -> text.isQuoted() && (e.getData(LangDataKeys.PSI_FILE)?.let { psiFile -> isJsSource(psiFile) } ?: false )
            } ?: false
    }

    private fun isJsSource(psiFile: PsiFile) = listOf("TypeScript", "JavaScript").contains(psiFile.fileType.name)

}