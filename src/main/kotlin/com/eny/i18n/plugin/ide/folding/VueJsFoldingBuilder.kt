package com.eny.i18n.plugin.ide.folding

import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlText
import com.intellij.util.ProcessingContext
import org.jetbrains.vuejs.lang.expr.parser.VueJSEmbeddedExprTokenType

/**
 * Vue Js i18n folding builder
 */
class VueJsFoldingBuilder: FoldingBuilderBase<JSCallExpression>(
    JSCallExpression::class.java,
    collectElementsOfType(XmlText::class.java, PlatformPatterns.psiElement(XmlText::class.java).with(TemplateText())),
    fun (root: PsiElement): Pair<List<PsiElement>, Int> {
        val vueParser = VueJSEmbeddedExprTokenType.createInterpolationExpression(root.project)
        val copy = trimLeft(unwrapTemplate(root))
        return Pair(
            collectChildren(vueParser.parseContents(copy.node).psi) { item -> item is JSLiteralExpression && JSPatterns.jsArgument("\$t", 0).accepts(item)},
            root.text.indexOf(copy.text)
        )
    }
)

/**
 * Pattern condition for embedded vue js elements
 */
class TemplateText : PatternCondition<XmlText>("vueContext") {
    override fun accepts(xmlText: XmlText, context: ProcessingContext?): Boolean {
        val text = xmlText.value.trim()
        return text.startsWith("{{") && text.endsWith("}}")
    }
}

/**
 * Unwraps contents of vue template
 */
fun unwrapTemplate(element: PsiElement): PsiElement {
    val copy = element.copy()
    val firstChild = copy.node.firstChildNode
    val lastChild = copy.node.lastChildNode
    if (firstChild.text == "{{") {
        copy.node.removeChild(firstChild)
    }
    if (lastChild.text == "}}") {
        copy.node.removeChild(lastChild)
    }
    return copy
}

/**
 * Trims left psi elements
 */
fun trimLeft(element: PsiElement): PsiElement {
    val copy = element.copy()
    copy.node.getChildren(null).takeWhile {
        it.text.isBlank()
    }.forEach {
        copy.node.removeChild(it)
    }
    return copy
}

/**
 * Utility function for collecting elements of Psi tree by predicate.
 * For cases when text gets parsed on the fly, it is impossible to use PsiTreeUtil, since it fails to check if elements are physical.
 */
fun collectChildren(element: PsiElement, predicate: (psiElement: PsiElement) -> Boolean): List<PsiElement> {
    val acc: MutableList<PsiElement> = mutableListOf()
    fun inner(item: PsiElement) {
        if (predicate (item)) {
            acc.add(item)
            //Won't go deeper:
            return
        } else {
            item.children.forEach {inner(it)}
        }
    }
    inner(element)
    return acc.toList()
}