package com.eny.i18n.extensions.lang.js.extractors

import com.eny.i18n.TranslationFunction
import com.eny.i18n.plugin.factory.FoldingProvider
import com.eny.i18n.plugin.utils.default
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

internal class JsFoldingProvider: FoldingProvider {
    override fun collectContainers(root: PsiElement, translationFunctions: List<TranslationFunction>): List<PsiElement> =
        PsiTreeUtil
            .findChildrenOfType(root, JSLiteralExpression::class.java)
            .filter { element ->
                translationFunctions.any {JSPatterns.jsArgument(it.name, it.argumentIndex).accepts(element)}
            }
    override fun getFoldingRange(container: PsiElement, offset: Int, psiElement: PsiElement): TextRange =
        PsiTreeUtil.getParentOfType(psiElement, JSCallExpression::class.java).default(psiElement).textRange
}
