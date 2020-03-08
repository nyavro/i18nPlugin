package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.language.psi.*
import com.eny.i18n.plugin.utils.LocalizationFileSearch
import com.eny.i18n.plugin.utils.PluginBundle
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
                visitPsiElement(o)
            }

            override fun visitNamespace(ns: I18nNamespace) {
                val files = LocalizationFileSearch(ns.project).findFilesByName(ns.text)
                if (files.isEmpty()) {
                    holder.registerProblem(ns, PluginBundle.getMessage("unresolved.ns"))
                }
            }

            override fun visitShortKey(o: I18nShortKey) {
                visitPsiElement(o)
            }
        }
    }
}