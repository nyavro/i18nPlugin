package com.eny.i18n.plugin.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.psi.PsiElementVisitor

/**
 * JavaScript inspection
 */
class JsInspection : LocalInspectionTool() {
    private val captures = listOf(
        JSPatterns.jsArgument("t", 0),
        JSPatterns.jsArgument("\$t", 0)
    )
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        val inspection = I18nInspection(captures, holder)
        return object : JSElementVisitor() {
            override fun visitJSLiteralExpression(node: JSLiteralExpression) = inspection.inspect(node)
        }
    }
}