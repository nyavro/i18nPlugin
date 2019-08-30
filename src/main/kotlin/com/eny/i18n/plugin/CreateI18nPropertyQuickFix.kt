package com.eny.i18n.plugin

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class CreateI18nPropertyQuickFix(fileName: String?, compositeKey: List<String>) : BaseIntentionAction() {

    override fun getFamilyName(): String {
        return "i18n resource"
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
    }

}
