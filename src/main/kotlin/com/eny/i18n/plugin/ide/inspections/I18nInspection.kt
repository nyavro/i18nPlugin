package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.language.psi.*
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
                process(o.compositeKey, ns)
            }

            override fun visitNamespace(ns: I18nNamespace) {
                val files = LocalizationFileSearch(ns.project).findFilesByName(ns.text)
                if (files.isEmpty()) {
                    holder.registerProblem(ns, PluginBundle.getMessage("inspection.unresolved.ns"))
                }
            }

            override fun visitShortKey(o: I18nShortKey) {
                process(o.compositeKey, null)
            }

            private fun process(o: I18nCompositeKey, ns: String?) {
                val compositeKey = o.text.split(".").toList()
                val mostResolvedReference = LocalizationFileSearch(o.project).findFilesByName(ns).map { file ->
                    resolveCompositeKey2(
                        compositeKey,
                        PsiElementTree.create(file)
                    )
                }.maxBy { v -> v.path.size }
                if (mostResolvedReference == null) return
                when {
                    mostResolvedReference.unresolved.isEmpty() && mostResolvedReference.element?.isLeaf() ?: false -> {
                    }
                    mostResolvedReference.unresolved.isEmpty() && mostResolvedReference.isPlural -> {
                    }
                    mostResolvedReference.unresolved.isEmpty() -> {
                        holder.registerProblem(o, PluginBundle.getMessage("inspection.reference.to.object"))
                    }
                    mostResolvedReference.unresolved.isEmpty() -> {
                    }
                    else -> {
                        val resolved = mostResolvedReference.path.joinToString(".").length + 1
                        holder.registerProblem(
                            o,
                            TextRange(
                                resolved,
                                o.textRange.length
                            ),
                            PluginBundle.getMessage("inspection.unresolved.key")
                        )
                    }
                }
            }
        }
    }
}