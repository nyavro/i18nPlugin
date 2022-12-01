package com.eny.i18n.extensions.localization.json

import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.tree.Tree
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil

/**
 * Tree wrapper around json psi tree
 */
class JsonElementTree(val element: PsiElement): Tree<PsiElement> {
    override fun value(): PsiElement = element
    override fun isTree(): Boolean = element is JsonObject
    override fun findChild(name: String): Tree<PsiElement>? =
        (element as JsonObject).findProperty(name)?.value?.let{ JsonElementTree(it) }
    override fun findChildren(prefix: String): List<Tree<PsiElement>> =
        element
            .node
            .getChildren(TokenSet.create(JsonElementTypes.PROPERTY))
            .asList()
            .map {item -> item.firstChildNode.psi}
            .filter {it.text.unQuote().startsWith(prefix)}
            .map {JsonElementTree(it)}

    companion object {
        /**
         * Creates instance of JsonElementTree
         */
        fun create(file: PsiElement): JsonElementTree? =
            PsiTreeUtil
                .getChildOfType(file, JsonObject::class.java)
                ?.let{ JsonElementTree(it)}
    }
}