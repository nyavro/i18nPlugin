package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.language.psi.I18nFullKey
import com.eny.i18n.plugin.language.psi.I18nNamespace
import com.eny.i18n.plugin.language.psi.I18nShortKey
import com.eny.i18n.plugin.language.psi.I18nVisitor
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.LocalizationFileSearch
import com.eny.i18n.plugin.utils.PluginBundle
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

class I18nInspection : LocalInspectionTool(), CompositeKeyResolver<PsiElement> {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : I18nVisitor() {
            override fun visitFullKey(o: I18nFullKey) {
                val ns = o.namespace.text
                val compositeKey = o.compositeKey.text.split(".").toList()
                val mostResolvedReference = LocalizationFileSearch(o.project).findFilesByName(ns).map {
                    file -> resolveCompositeKey2(
                        compositeKey,
                        PsiElementTree.create(file)
                    )
                }.maxBy {v -> v.path.size}
                if (mostResolvedReference == null) return
                when {
                    mostResolvedReference.unresolved.isEmpty() && mostResolvedReference.element?.isLeaf() ?: false -> {}
                    mostResolvedReference.unresolved.isEmpty() && mostResolvedReference.isPlural -> {}
                    mostResolvedReference.unresolved.isEmpty() -> {}
                    mostResolvedReference.unresolved.isEmpty() -> {}
                    else -> {
                        val resolved = mostResolvedReference.path.joinToString(".").length + 1
                        holder.registerProblem(
                            o.compositeKey,
                            TextRange(
                                resolved,
                                o.compositeKey.textRange.length
                            ),
                            PluginBundle.getMessage("unresolved.key")
                        )
                    }
                }
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