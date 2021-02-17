package com.eny.i18n.plugin.key

import com.eny.i18n.plugin.factory.CallContext
import com.eny.i18n.plugin.parser.Extractor
import com.intellij.psi.PsiElement

/**
 *
 */
class FullKeyExtractor(val context: CallContext, val extractor: Extractor): Extractor {
    override fun extractFullKey(element: PsiElement): FullKey? =
        if (context.accepts(element)) extractor.extractFullKey(element)
        else null
}