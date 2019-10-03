package com.eny.i18n.plugin.tree

import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.eny.i18n.plugin.utils.unQuote

class PsiElementTree(val element: PsiElement) : Tree<PsiElement> {
    override fun value(): PsiElement = element
    override fun isTree(): Boolean = element is JsonObject
    override fun findChild(name: String): PsiElementTree? =
        if (element is JsonObject) element.findProperty(name)?.value?.let { child -> PsiElementTree(child) }
        else null
    override fun findChildren(regex: Regex): List<Tree<PsiElement>> =
        element
            .node
            .getChildren(TokenSet.create(JsonElementTypes.PROPERTY))
            .asList()
            .map {item -> item.firstChildNode.psi}
            .filter {item -> item != null && item.text.unQuote().matches(regex)}
            .map {item -> PsiElementTree(item)}
}