package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.LocalizationFileSearch
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement

/**
 * Resolves and annotates given full key.
 */
class FullKeyAnnotator(val holder: ProblemsHolder) : CompositeKeyResolver<PsiElement> {

    /**
     * Annotates full key
     */
    fun annotateI18nLiteral(fullKey:FullKey, element: PsiElement) {
        val fileName = fullKey.ns?.text
        val compositeKey = fullKey.compositeKey
        val settings = Settings.getInstance(element.project)
        val annotationHelper = AnnotationHelper(holder, element)
        val files = LocalizationFileSearch(element.project).findFilesByName(fileName)
        if (files.isEmpty()) {
            val ns = fullKey.ns
            if (ns != null) {
                annotationHelper.unresolvedNs(fullKey)
            }
        }
        else {
            val pluralSeparator = settings.pluralSeparator
            val mostResolvedReference = files
                .flatMap { jsonFile -> resolve(compositeKey, PsiElementTree.create(jsonFile), pluralSeparator) }
                .maxBy { v -> v.path.size }!!
            when {
                mostResolvedReference.unresolved.isEmpty() -> annotationHelper.referenceToObject(fullKey)
                fullKey.ns == null && (fullKey.compositeKey.size == mostResolvedReference.unresolved.size) -> {}
                else -> annotationHelper.unresolvedKey(fullKey, mostResolvedReference)
            }
        }
    }
}
