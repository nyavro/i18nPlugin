package com.eny.i18n.plugin.tree

import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement

class PsiElementTree(val node: PsiElement) : Tree<PsiElement> {
    override fun value(): PsiElement = node
    override fun isTree(): Boolean = node is JsonObject
    override fun findChild(name: String): PsiElementTree? =
        if (node is JsonObject) node.findProperty(name)?.value?.let { child -> PsiElementTree(child) }
        else null
}