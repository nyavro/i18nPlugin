package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.language.psi.*
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor

class I18nInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : I18nVisitor() {
            override fun visitCompositeKey(o: I18nCompositeKey) {
                visitPsiElement(o)
            }

            override fun visitFullKey(o: I18nFullKey) {
                holder.registerProblem(o, "TEST")
            }

            override fun visitNamespace(o: I18nNamespace) {
                visitPsiElement(o)
            }

            override fun visitShortKey(o: I18nShortKey) {
                visitPsiElement(o)
            }
        }
    }
}