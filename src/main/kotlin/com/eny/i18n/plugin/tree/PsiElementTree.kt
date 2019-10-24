package com.eny.i18n.plugin.tree

import com.eny.i18n.plugin.utils.unQuote
import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.TokenSet

class PsiElementTree(val element: PsiElement) : Tree<PsiElement>, FlippedTree<PsiElement> {
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
    override fun getParent(): FlippedTree<PsiElement>? {
        fun findParent(current: PsiElement): PsiElement? =
            if (current is JsonProperty) current
            else if (current is PsiFile) null
            else findParent(current.parent)
        return findParent(element)?.let { parent -> PsiElementTree(parent)}
    }
    override fun name(): String = element.firstChild.text.unQuote()
}