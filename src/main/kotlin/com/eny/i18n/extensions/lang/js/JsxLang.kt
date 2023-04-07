package com.eny.i18n.extensions.lang.js

import com.eny.i18n.TranslationFunction
import com.eny.i18n.plugin.factory.FoldingProvider
import com.eny.i18n.plugin.factory.TranslationExtractor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

class JsxLang : JsLang() {

    override fun canExtractKey(element: PsiElement, translationFunctions: List<TranslationFunction>): Boolean = false

    /**
     * No special implementation for folding builder is required
     */
    override fun foldingProvider(): FoldingProvider = object: FoldingProvider {
        override fun collectContainers(root: PsiElement, translationFunctions: List<TranslationFunction>): List<PsiElement> = emptyList()
        override fun getFoldingRange(container: PsiElement, offset: Int, psiElement: PsiElement): TextRange = TextRange.EMPTY_RANGE
    }

    override fun translationExtractor(): TranslationExtractor = JsxTranslationExtractor()
}
