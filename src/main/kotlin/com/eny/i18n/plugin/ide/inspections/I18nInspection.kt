package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.parser.FullKeyExtractor
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.lang.javascript.psi.ecma6.JSStringTemplateExpression
import com.intellij.psi.PsiElementVisitor

/**
 * I18n inspection
 */
class I18nInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : JSElementVisitor() {

            private val annotator = FullKeyAnnotator(holder)
            private val keyExtractor = FullKeyExtractor()

            override fun visitJSStringTemplateExpression(stringTemplateExpression: JSStringTemplateExpression) {
                visitJSExpression(stringTemplateExpression)
            }
            override fun visitJSLiteralExpression(node: JSLiteralExpression) {
                val i18nKeyLiteral = keyExtractor.extractI18nKeyLiteral(node)
                if (i18nKeyLiteral != null) {
                    annotator.annotateI18nLiteral(i18nKeyLiteral, node)
                }
            }
        }
    }
}