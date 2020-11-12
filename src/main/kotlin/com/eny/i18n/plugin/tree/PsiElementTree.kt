package com.eny.i18n.plugin.tree

import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.CollectingSequence
import com.eny.i18n.plugin.utils.at
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parents
import com.intellij.psi.util.parentsOfType
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

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
            else if (file is JsonFile) JsonElementTree.create(file)
            else if (file is JsonObject) JsonElementTree(file)
//            else if (file is JSObjectLiteralExpression) JsElementTree.create(file)
            else YamlElementTree.create(file)
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
 * Tree wrapper around json psi tree
 */
class JsonElementTree(val element: PsiElement): PsiElementTree() {
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


/**
 * Tree wrapper around yaml psi tree
 */
class YamlElementTree(val element: PsiElement): PsiElementTree() {
    override fun value(): PsiElement = element
    override fun isTree(): Boolean = element is YAMLMapping
    override fun findChild(name: String): Tree<PsiElement>? =
        (element as YAMLMapping).getKeyValueByKey(name)?.value?.let(::YamlElementTree)
    override fun findChildren(prefix: String): List<Tree<PsiElement>> =
        (element as YAMLMapping)
            .keyValues
            .filter {it.key?.text?.startsWith(prefix) ?: false}
            .mapNotNull {it.key?.let (::YamlElementTree)}
    companion object {
        /**
         * Creates YamlElementTree instance
         */
        fun create(file: PsiElement): YamlElementTree? {
            val fileRoot = PsiTreeUtil.getChildOfType(file, YAMLDocument::class.java)
            return (PsiTreeUtil.getChildOfType(fileRoot, YAMLMapping::class.java) ?: fileRoot)?.let {YamlElementTree(it)}
        }
    }
}
