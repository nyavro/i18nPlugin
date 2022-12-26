package com.eny.i18n.extensions.lang.js

import com.eny.i18n.plugin.factory.FoldingProvider
import com.eny.i18n.plugin.factory.TranslationExtractor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

class JsxLang : JsLang() {

    override fun canExtractKey(element: PsiElement, translationFunctionNames: List<String>): Boolean = false

    /**
     * No special implementation for folding builder is required
     */
    override fun foldingProvider(): FoldingProvider = object: FoldingProvider {
        override fun collectContainers(root: PsiElement): List<PsiElement> = emptyList()
        override fun collectLiterals(container: PsiElement): Pair<List<PsiElement>, Int> = Pair(emptyList(), 0)
        override fun getFoldingRange(container: PsiElement, offset: Int, psiElement: PsiElement): TextRange = TextRange.EMPTY_RANGE
    }

    override fun translationExtractor(): TranslationExtractor = JsxTranslationExtractor()
}
