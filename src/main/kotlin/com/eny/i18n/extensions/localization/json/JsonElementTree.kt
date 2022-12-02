package com.eny.i18n.extensions.localization.json

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


/**
 * Tree wrapper around js psi tree
 */
//class JsElementTree(val element: PsiElement): PsiElementTree() {
//    override fun value(): PsiElement = element
//    override fun isTree(): Boolean = element is JSObjectLiteralExpression
//    override fun findChild(name: String): Tree<PsiElement>? {
//        return (element as? JSObjectLiteralExpression)
//            ?.findProperty(name)
//            ?.value
//            ?.let { child ->
//                if (child is JSReferenceExpression) {
//                    resolveReferenceChain(child, 10)?.let {
//                        JsElementTree(it)
//                    }
//                } else {
//                    JsElementTree(child)
//                }
//            }
//    }
//    private fun resolveReferenceChain(element: JSReferenceExpression, depth: Int):JSObjectLiteralExpression? {
//        val res = element.reference?.resolve()
//        if (res is JSVariable) {
//            val rvalue = res.children[0]
//            if (rvalue is JSReferenceExpression && depth > 0) {
//                return resolveReferenceChain(rvalue, depth-1)
//            }
//            return rvalue as? JSObjectLiteralExpression
//        } else {
//            return res as? JSObjectLiteralExpression
//        }
//    }
//    override fun findChildren(regex: Regex): List<Tree<PsiElement>> {
//        return listOf(element.node)
//            .map { item -> item.firstChildNode.psi }
//            .filter { item -> item != null && item.text.unQuote().matches(regex) }
//            .map { item -> JsElementTree(item) }
//    }
//    companion object {
//        /**
//         * Creates instance of JsElementTree
//         */
//        fun create(file: PsiElement): JsElementTree? = JsElementTree(file)
//    }
//}
