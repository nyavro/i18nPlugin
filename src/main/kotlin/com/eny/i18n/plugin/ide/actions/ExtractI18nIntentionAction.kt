package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.*
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.lang.ecmascript6.JSXHarmonyFileType
import com.intellij.lang.javascript.TypeScriptJSXFileType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.html.HtmlTag
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlToken
import org.jetbrains.vuejs.lang.html.VueFileType

internal interface Extractor {
    fun canExtract(element: PsiElement): Boolean
    fun text(element: PsiElement): String = element.text.unQuote()
    fun textRange(element: PsiElement): TextRange = element.parent.textRange
    fun template(element: PsiElement): (argument: String) -> String = {"i18n.t($it)"}
}

internal class JsDialectExtractor: Extractor {
    override fun canExtract(element: PsiElement): Boolean = "JS:STRING_LITERAL" == element.type()
}

internal class JsxDialectExtractor: Extractor {
    override fun canExtract(element: PsiElement): Boolean =
        listOf(JSXHarmonyFileType.INSTANCE, TypeScriptJSXFileType.INSTANCE).any { it == element.containingFile.fileType } &&
        !PsiTreeUtil.findChildOfType(PsiTreeUtil.getParentOfType(element, XmlTag::class.java), XmlTag::class.java).toBoolean()

    override fun text(element: PsiElement): String {
        val parentOfType = PsiTreeUtil.getParentOfType(element, XmlTag::class.java)
        return if (parentOfType != null) {
            parentOfType.value.textElements.map { item -> item.text}.joinToString(" ")
        } else {
            element.parent.text
        }
    }
    override fun textRange(element: PsiElement): TextRange =
        PsiTreeUtil.getParentOfType(element, XmlTag::class.java)
            ?.value
            ?.textElements
            ?.let {
                TextRange(
                    it.first().textRange.startOffset,
                    it.last().textRange.endOffset
                )
            } ?:
            TextRange(
                element.parent.textRange.startOffset,
                element.parent.textRange.endOffset
            )
}

internal class PhpExtractor: Extractor {
    override fun canExtract(element: PsiElement): Boolean =
        listOf("double quoted string", "single quoted string").contains(element.type())
    override fun template(element: PsiElement): (argument: String) -> String = {"t($it)"}
}

internal class VueExtractor: Extractor {
    override fun canExtract(element: PsiElement): Boolean = element.isVue()
    override fun text(element: PsiElement): String =
        element.whenMatches { it.isVueText() }?.parent.default(element).text.unQuote()
    override fun textRange(element: PsiElement): TextRange =
        element.whenMatches { it.isVueText() }?.parent.default(element).textRange
    override fun template(element: PsiElement): (argument: String) -> String =
        when {
            element.isVueTemplate() -> ({"this.\$t($it)"})
            element.isVue() -> ({"{{ \$t($it) }}"})
            else -> ({"\$t($it)"})
        }

    private fun PsiElement.isVue():Boolean = this.containingFile.fileType == VueFileType.INSTANCE

    private fun PsiElement.isVueTemplate():Boolean = this.isVue() && PsiTreeUtil.findFirstParent(this, {it is HtmlTag && it.name == "script"}).toBoolean()

    private fun PsiElement.isVueText(): Boolean = (this is PsiWhiteSpace) || (this is XmlToken)
}

internal class DefaultExtractor: Extractor {
    override fun canExtract(element: PsiElement): Boolean = false
}

/**
 * Intention action of i18n key extraction
 */
class ExtractI18nIntentionAction : PsiElementBaseIntentionAction(), IntentionAction {

    private val request = KeyRequest()

    private val keyExtractor = KeyExtractor()

    override fun getText() = PluginBundle.getMessage("action.intention.extract.key")

    override fun getFamilyName() = "ExtractI18nIntentionAction"

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) =
        ApplicationManager.getApplication().invokeLater {
            doInvoke(editor, project, element)
        }

    private fun getExtractor(element: PsiElement, isVue: Boolean): Extractor {
        val extractor = (if (isVue) listOf(VueExtractor()) else listOf(
            JsDialectExtractor(), JsxDialectExtractor(), PhpExtractor())).find { it.canExtract(element) }
        return extractor ?: DefaultExtractor()
    }

    private fun doInvoke(editor: Editor?, project: Project, element: PsiElement) {
        editor ?: return
        val document = editor.document
        val primaryCaret = editor.caretModel.primaryCaret
        val settings = Settings.getInstance(project)
        val extractor = getExtractor(element, settings.vue)
        val text = extractor.text(element)
        val requestResult = request.key(project, text)
        if (requestResult.isCancelled) return
        if (requestResult.key == null) {
            Messages.showInfoMessage(
                PluginBundle.getMessage("action.intention.extract.key.invalid"),
                PluginBundle.getMessage("action.intention.extract.key.invalid.title")
            )
            return
        }
        val i18nKey = requestResult.key
        val template = extractor.template(element)
        val range = extractor.textRange(element)
        WriteCommandAction.runWriteCommandAction(project) {
            keyExtractor.tryToResolveTranslationFile(project, i18nKey, text, editor, {
                document.replaceString(range.startOffset, range.endOffset, template("'${i18nKey.source}'"))
            })
        }
        primaryCaret.removeSelection()
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return getExtractor(element, Settings.getInstance(project).vue).canExtract(element)
    }
}

