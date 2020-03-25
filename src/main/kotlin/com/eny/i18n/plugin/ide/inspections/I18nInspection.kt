package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.parser.CaptureContext
import com.eny.i18n.plugin.parser.FullKeyExtractor
import com.eny.i18n.plugin.parser.KeyExtractorImpl
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement

/**
 * Inspects i18n key
 */
class I18nInspection<P: PsiElement, T: PsiElementPattern<P, T>>(captures: List<T>, holder: ProblemsHolder) {
    private val annotator = FullKeyAnnotator(holder)
    private val keyExtractor = FullKeyExtractor(CaptureContext(captures), KeyExtractorImpl())
    fun inspect(node: P) {
        val key = keyExtractor.extractI18nKeyLiteral(node)
        if (key != null) {
            annotator.annotateI18nLiteral(key, node)
        }
    }
}