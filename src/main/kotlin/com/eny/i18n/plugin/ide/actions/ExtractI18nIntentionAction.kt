package com.eny.i18n.plugin.ide.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

/**
 * Intention action of i18n key extraction
 */
class ExtractI18nIntentionAction : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText() = "Intention action test"

    override fun getFamilyName() = "ExtractI18nIntentionAction"

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {

    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean =
        element.containingFile.fileType.name == "TypeScript"
}