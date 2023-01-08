package com.eny.i18n.extensions.localization.js

import com.eny.i18n.plugin.tree.Tree
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.lang.javascript.psi.JSObjectLiteralExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

private class TsObjectLiteralTree (val objectLiteral: JSObjectLiteralExpression): Tree<PsiElement> {

    override fun findChild(name: String): Tree<PsiElement>? = objectLiteral.findProperty(name)?.let{ property -> property.value?.let{TsLocalizationTree.create(it)} }

    override fun isTree(): Boolean = true

    override fun value(): PsiElement = objectLiteral

    override fun findChildren(prefix: String): List<Tree<PsiElement>> =
        objectLiteral
            .properties
            .filter {it.name?.startsWith(prefix) ?: false}
            .mapNotNull { property -> property.value?.let{TsLocalizationTree.create(it)} }
}

private class TsLiteralExpressionTree (val literalExpression: JSLiteralExpression): Tree<PsiElement> {

    override fun findChild(name: String): Tree<PsiElement>? = null

    override fun isTree(): Boolean = false

    override fun value(): PsiElement = literalExpression

    override fun findChildren(prefix: String): List<Tree<PsiElement>> = listOf()
}

class TsLocalizationTree {

    companion object {
        fun create(element: PsiElement): Tree<PsiElement>? {
            return if(element is JSObjectLiteralExpression) {
                TsObjectLiteralTree(element)
            } else if(element is JSLiteralExpression) {
                TsLiteralExpressionTree(element)
            } else {
                PsiTreeUtil.findChildOfType(element, JSObjectLiteralExpression::class.java)?.let {
                    TsObjectLiteralTree(it)
                }
            }
        }
    }
}
