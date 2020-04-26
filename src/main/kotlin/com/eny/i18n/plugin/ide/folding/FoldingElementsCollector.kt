package com.eny.i18n.plugin.ide.folding

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Generates elements collector function
 */
fun <T: PsiElement, E: T, P: PsiElementPattern<T, P>> collectElementsOfType(cls: Class<E>, pattern: P): (root: PsiElement) -> List<T> =
    fun (root:PsiElement) = PsiTreeUtil.findChildrenOfType(root, cls).filter {pattern.accepts(it)}