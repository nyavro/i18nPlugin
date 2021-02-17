package com.eny.i18n.plugin.tree

import com.eny.i18n.plugin.ide.settings.mainFactory
import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.at
import com.intellij.psi.PsiElement

/**
 * Represents wrapper-tree around psi tree
 */
abstract class PsiElementTree: Tree<PsiElement> {
    companion object {
        /**
         * Creates instance of PsiElementTree
         */
        fun create(file: PsiElement): PsiElementTree? =
            if (file.containingFile?.virtualFile?.extension == "po") PlainTextTree.create(file)
            else {
                file.project.mainFactory().localizationFactories().mapNotNull {
                    it.elementTreeFactory()(file)
                }.firstOrNull()
            }
    }
}

/**
 * Plain text file wrapper
 */
class PlainTextTree(val element: PsiElement): PsiElementTree() {

    override fun findChild(name: String): Tree<PsiElement>? {
        return element.children.find {
            it.type()=="SECTION" &&
            it.children.at(0)?.let {
                it.type()=="ID_LINE" && it.toString()==name
            } == true
        }?.children?.at(0)?.let {PlainTextTree(it)}
    }

    override fun isTree(): Boolean {
        return element == element.containingFile
    }

    override fun value(): PsiElement = element.nextSibling.nextSibling.let{it.children.at(0) ?: it}

    override fun findChildren(prefix: String): List<Tree<PsiElement>> {
        TODO("Not yet implemented")
    }

    companion object {
        /**
         * Creates instance of PlainTextTree
         */
        fun create(file: PsiElement): PsiElementTree? = PlainTextTree(file)
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


