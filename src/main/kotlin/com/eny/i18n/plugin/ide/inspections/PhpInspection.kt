package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.language.php.PhpPatternsExt
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

/**
 * PHP inspection
 */
class PhpInspection : LocalInspectionTool() {
    private val captures = listOf(PhpPatternsExt.phpArgument("t", 0))
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        val inspection = I18nInspection(captures, holder)
        return object : PhpElementVisitor() {
            override fun visitPhpStringLiteralExpression(node: StringLiteralExpression) {
                inspection.inspect(node)
            }
        }
    }
}