package com.eny.i18n.plugin.tree

import com.eny.i18n.plugin.utils.unQuote
import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.TokenSet

class PsiElementTree(val element: PsiElement): Tree<PsiElement> {
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
class PsiRoot(val element: PsiFile): FlippedTree<PsiElement> {
    override fun name() = element.containingFile.name
    override fun isRoot() = true
    override fun ancestors(): List<FlippedTree<PsiElement>> = listOf()
}
class PsiProperty(val element: JsonProperty): FlippedTree<PsiElement> {
    override fun name() = element.firstChild.text.unQuote()
    override fun isRoot() = false
    override fun ancestors(): List<FlippedTree<PsiElement>> {
        fun allAncestors(item: PsiElement): List<FlippedTree<PsiElement>> {
            if (item is PsiFile) return listOf(PsiRoot(item))
            else if(item is JsonProperty) return allAncestors(item.parent) + PsiProperty(item)
            else return allAncestors(item.parent)
        }
        return allAncestors(element)
    }
}