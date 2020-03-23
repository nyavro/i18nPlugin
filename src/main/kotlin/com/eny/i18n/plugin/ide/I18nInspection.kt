package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.parser.FullKeyExtractor
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.LocalizationFileSearch
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.lang.javascript.psi.ecma6.JSStringTemplateExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

class I18nInspection : LocalInspectionTool(), CompositeKeyResolver<PsiElement> {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : JSElementVisitor() {

            private val keyExtractor = FullKeyExtractor()

            override fun visitJSStringTemplateExpression(stringTemplateExpression: JSStringTemplateExpression) {
                visitJSExpression(stringTemplateExpression)
            }
            override fun visitJSLiteralExpression(node: JSLiteralExpression) {
                val i18nKeyLiteral = keyExtractor.extractI18nKeyLiteral(node)
                if (i18nKeyLiteral != null) {
                    annotateI18nLiteral(i18nKeyLiteral, node)
                }
            }
            private fun annotateI18nLiteral(fullKey: FullKey, element: PsiElement) {
                val fileName = fullKey.ns?.text
                val compositeKey = fullKey.compositeKey
                val settings = Settings.getInstance(element.project)
                val annotationHelper = AnnotationHelper2(holder, element)
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
    }
}