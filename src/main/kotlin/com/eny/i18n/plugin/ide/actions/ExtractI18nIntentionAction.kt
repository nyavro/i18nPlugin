package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.PluginBundle
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiElement

/**
 * Intention action of i18n key extraction
 */
class ExtractI18nIntentionAction : PsiElementBaseIntentionAction(), IntentionAction {

    private val request = KeyRequest()

    private val keyExtractor = KeyExtractor()

    override fun getText() = PluginBundle.getMessage("action.intention.extract.key")

    override fun getFamilyName() = "ExtractI18nIntentionAction"

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        editor ?: return
        val document = editor.document
        val primaryCaret = editor.caretModel.primaryCaret
//        val selectedText = primaryCaret.selectedText ?: return
        val requestResult = request.key(project)
        if (requestResult.isCancelled) return
        if (requestResult.key == null) {
            Messages.showInfoMessage(
                PluginBundle.getMessage("action.intention.extract.key.invalid"),
                PluginBundle.getMessage("action.intention.extract.key.invalid.title")
            )
            return
        }
        val i18nKey = requestResult.key
        val settings = Settings.getInstance(project)
        val template = settings.translationFunction
        val range = element.textRange
        val text = element.text.unQuote()
        WriteCommandAction.runWriteCommandAction(project) {
            keyExtractor.tryToResolveTranslationFile(project, i18nKey, text, editor)
            document.replaceString(range.startOffset, range.endOffset, "$template('${i18nKey.source}')")
        }
        primaryCaret.removeSelection()
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
//        TypeScript JSX
//        XML_DATA_CHARACTERS
        return (element.type()=="JS:STRING_LITERAL")
    }
}