package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.PluginBundle
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlTag

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
        val range = getTextRange(element)
        val text = getText(element)
        WriteCommandAction.runWriteCommandAction(project) {
            keyExtractor.tryToResolveTranslationFile(project, i18nKey, text, editor)
            document.replaceString(range.startOffset, range.endOffset, "$template('${i18nKey.source}')")
        }
        primaryCaret.removeSelection()
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
//        TypeScript JSX
//        XML_DATA_CHARACTERS
        return if (element.containingFile.fileType.name == "JSX Harmony") {
            PsiTreeUtil.findChildOfType(PsiTreeUtil.getParentOfType(element, XmlTag::class.java), XmlTag::class.java) == null
        }
        else {
            listOf("XML_DATA_CHARACTERS", "JS:STRING_LITERAL").contains(element.type())
        }
    }

    private fun getText(element: PsiElement): String =
        if (element.containingFile.fileType.name == "JSX Harmony") {
            PsiTreeUtil.getParentOfType(element, XmlTag::class.java)?.value?.textElements?.map {item -> item.text}?.joinToString(" ") ?: element.text
        }
        else element.text

    private fun getTextRange(element: PsiElement): TextRange =
        if (element.containingFile.fileType.name == "JSX Harmony") {
            val textElements = PsiTreeUtil.getParentOfType(element, XmlTag::class.java)?.value?.textElements
            TextRange(
                (textElements?.firstOrNull() ?: element).textRange.startOffset,
                (textElements?.lastOrNull() ?: element).textRange.endOffset
            )
        }
        else element.textRange
}