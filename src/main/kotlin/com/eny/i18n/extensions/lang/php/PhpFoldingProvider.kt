package com.eny.i18n.extensions.lang.php

import com.eny.i18n.TranslationFunction
import com.eny.i18n.plugin.factory.FoldingProvider
import com.eny.i18n.plugin.utils.default
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.FunctionReference
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression

internal class PhpFoldingProvider: FoldingProvider {
    override fun collectContainers(root: PsiElement, translationFunctions: List<TranslationFunction>): List<PsiElement> =
        PsiTreeUtil
            .findChildrenOfType(root, StringLiteralExpression::class.java)
            .filter { element ->
                translationFunctions.any {PhpPatternsExt.phpArgument(it.name, it.argumentIndex).accepts(element)}
            }

    override fun getFoldingRange(container: PsiElement, offset: Int, psiElement: PsiElement): TextRange =
        PsiTreeUtil.getParentOfType(psiElement, FunctionReference::class.java).default(psiElement).textRange
}
