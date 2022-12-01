package com.eny.i18n.plugin.tree

import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.at
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLMapping

/**
 * Represents wrapper-tree around psi tree
 */
abstract class PsiElementTree: Tree<PsiElement> {
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


