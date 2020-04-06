package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.language.php.PhpPatternsExt
import com.eny.i18n.plugin.parser.CaptureContext
import com.eny.i18n.plugin.parser.FullKeyExtractor
import com.eny.i18n.plugin.parser.KeyExtractorImpl
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

/**
 * i18n annotator for php
 */
class PhpCompositeKeyAnnotator : Annotator, CompositeKeyResolver<PsiElement> {

    private val annotator = CompositeKeyAnnotator(
        FullKeyExtractor(
            CaptureContext(listOf(
                PhpPatternsExt.phpArgument("t", 0)
            )),
            KeyExtractorImpl()
        )
    )

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        annotator.annotate(element, holder)
    }
}