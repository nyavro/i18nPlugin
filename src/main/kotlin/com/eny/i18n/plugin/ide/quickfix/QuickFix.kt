package com.eny.i18n.plugin.ide.quickfix

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * Common quick fix interface
 */
abstract class QuickFix: BaseIntentionAction() {

    override fun getFamilyName(): String = "i18n quick fix"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor != null) {
            invoke(project, editor)
        }
    }

    /**
     * Run quick fix
     */
    abstract fun invoke(project: Project, editor: Editor)
}