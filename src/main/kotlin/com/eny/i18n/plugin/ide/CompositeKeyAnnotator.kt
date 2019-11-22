package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.*
import com.intellij.json.psi.JsonObject
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Annotator for i18n keys
 */
class CompositeKeyAnnotator : Annotator, CompositeKeyResolver<PsiElement> {

    private val jsUtil = JavaScriptUtil()

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val i18nKeyLiteral = jsUtil.extractI18nKeyLiteral(element)
        if (i18nKeyLiteral != null) {
            annotateI18nLiteral(i18nKeyLiteral, element, holder)
        }
    }

    private fun annotateI18nLiteral(fullKey: FullKey, element: PsiElement, holder: AnnotationHolder) {
        val fileName = fullKey.ns?.text
        val compositeKey = fullKey.compositeKey
        val annotationHelper = AnnotationHelper(holder, AnnotationHolderFacade(element.textRange))
        val files = JsonSearchUtil(element.project).findFilesByName(fileName)
        if (files.isEmpty()) annotationHelper.annotateFileUnresolved(fullKey)
        else {
            val pluralSeparator = Settings.getInstance(element.project).pluralSeparator
            val mostResolvedReference = files
                .flatMap { jsonFile ->
                    tryToResolvePlural(
                        resolveCompositeKey(
                            compositeKey,
                            PsiTreeUtil.getChildOfType(jsonFile, JsonObject::class.java)?.let{fileRoot -> PsiElementTree(fileRoot)}
                        ),
                        pluralSeparator
                    )
                }
                .maxBy {v -> v.path.size}!!
            when {
                mostResolvedReference.unresolved.isEmpty() && mostResolvedReference.element?.isLeaf() ?: false -> annotationHelper.annotateResolved(fullKey)
                mostResolvedReference.unresolved.isEmpty() && mostResolvedReference.isPlural-> annotationHelper.annotateReferenceToPlural(fullKey)
                mostResolvedReference.unresolved.isEmpty() && fullKey.isTemplate -> annotationHelper.annotatePartiallyResolved(fullKey, mostResolvedReference.path)
                mostResolvedReference.unresolved.isEmpty() -> annotationHelper.annotateReferenceToJson(fullKey)
                else -> annotationHelper.annotateUnresolved(fullKey, mostResolvedReference.path)
            }
        }
    }
}