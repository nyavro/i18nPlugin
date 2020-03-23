package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.parser.FullKeyExtractor
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

/**
 * I18n PHP inspection
 */
class I18nPhpInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {

            private val annotator = FullKeyAnnotator(holder)
            private val keyExtractor = FullKeyExtractor()

            override fun visitPhpStringLiteralExpression(stringTemplateExpression: StringLiteralExpression) {
                val i18nKeyLiteral = keyExtractor.extractI18nKeyLiteral(stringTemplateExpression)
                if (i18nKeyLiteral != null) {
                    annotator.annotateI18nLiteral(i18nKeyLiteral, stringTemplateExpression)
                }
            }
        }
    }
}