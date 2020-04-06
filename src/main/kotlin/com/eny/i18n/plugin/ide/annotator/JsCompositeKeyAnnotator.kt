package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.parser.CaptureContext
import com.eny.i18n.plugin.parser.FullKeyExtractor
import com.eny.i18n.plugin.parser.KeyExtractorImpl
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.psi.PsiElement

/**
 * i18n annotator for js
 */
class JsCompositeKeyAnnotator : Annotator, CompositeKeyResolver<PsiElement> {

    private val annotator = CompositeKeyAnnotator(
        FullKeyExtractor(
            CaptureContext(listOf(
                JSPatterns.jsArgument("t", 0),
                JSPatterns.jsArgument("\$t", 0)
            )),
            KeyExtractorImpl()
        )
    )

    override fun annotate(element: PsiElement, holder: AnnotationHolder) = annotator.annotate(element, holder)
}