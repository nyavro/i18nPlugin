package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.tree.PropertyReference
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.PluginBundle
import com.eny.i18n.plugin.utils.tokensLength
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

/**
 * Helps to annotate I18n key
 */
class AnnotationHelper(val holder: ProblemsHolder, val element: PsiElement) {
    fun referenceToObject(fullKey: FullKey) {
        holder.registerProblem(element, TextRange((fullKey.ns?.length ?: -1) + 1 + 1, fullKey.source.length+1), PluginBundle.getMessage("inspection.object.reference"))
    }

    fun unresolvedKey(fullKey: FullKey, mostResolvedReference: PropertyReference<PsiElement>) {
        val i = (fullKey.ns?.length ?: -1) + 1 + tokensLength(mostResolvedReference.path) + 1
        holder.registerProblem(element, TextRange(i + 1, fullKey.source.length + 1), PluginBundle.getMessage("inspection.unresolved.key"))
    }

    fun unresolvedNs(fullKey: FullKey) {
        holder.registerProblem(element, TextRange(1, (fullKey.ns?.length ?: 0) +1), PluginBundle.getMessage("unresolved.ns"))
    }
}
