package com.eny.i18n.extensions.source.providers

import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.tree.Tree
import com.eny.i18n.plugin.utils.at
import com.intellij.psi.PsiElement

/**
 * Plain text file wrapper
 */
class PlainTextTree(val element: PsiElement): Tree<PsiElement> {

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
        fun create(file: PsiElement): Tree<PsiElement>? = PlainTextTree(file)
    }
}