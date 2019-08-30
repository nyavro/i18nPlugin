package com.eny.i18n.plugin

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class Create18nJsonFileQuickFix(fileName: String) : BaseIntentionAction() {
    override fun getFamilyName(): String {
        return "i18n plugin"
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
    }
}
