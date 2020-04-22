package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.language.php.PhpPatternsExt
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlText
import com.intellij.psi.xml.XmlToken
import com.intellij.util.ProcessingContext
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import org.jetbrains.vuejs.lang.expr.parser.VueJSEmbeddedExprTokenType

/**
 * Collects elements to extract i18n keys
 */
interface FoldingElementsCollector {
    /**
     * Collects literal container elements. In most cases string literal is self container.
     * For html based dialects first appropriate xml elements gets collected, then parsed and literals gets extracted
     */
    fun collectFoldingContainers(root: PsiElement): List<PsiElement>

    /**
     * Collects literals inside container.
     */
    fun collectLiterals(container: PsiElement): List<PsiElement> = listOf(container)
}

/**
 * Folding elements collector for JS language
 */
class FoldingElementsCollectorJs : FoldingElementsCollector {
    private val pattern = JSPatterns.jsArgument("t", 0)
    override fun collectFoldingContainers(root: PsiElement): List<PsiElement> {
        return PsiTreeUtil
            .findChildrenOfType(root, JSLiteralExpression::class.java)
            .filter { pattern.accepts(it) }
    }
}

/**
 * Folding elements collector for PHP language
 *
 * TODO - generalize with Js collector and? Vue collector
 */
class FoldingElementsCollectorPhp : FoldingElementsCollector {
    private val pattern = PhpPatternsExt.phpArgument("t", 0)
    override fun collectFoldingContainers(root: PsiElement): List<PsiElement> {
        return PsiTreeUtil
            .findChildrenOfType(root, StringLiteralExpression::class.java)
            .filter { pattern.accepts(it) }
    }
}

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
 * Folding elements collector for Vue language
 */
class FoldingElementsCollectorVue : FoldingElementsCollector {
    val templatePattern = PlatformPatterns.psiElement(XmlToken::class.java).withParent(XmlPatterns.xmlText().with(TemplateText()))
    val pattern = JSPatterns.jsArgument("\$t", 0)

    override fun collectFoldingContainers(root: PsiElement): List<PsiElement> {
        return PsiTreeUtil
            .findChildrenOfType(root, XmlToken::class.java)
            .filter { templatePattern.accepts(it) }
    }

    override fun collectLiterals(root: PsiElement): List<PsiElement> {
        val vueParser = VueJSEmbeddedExprTokenType.createInterpolationExpression(root.project)
        return collectChildren(vueParser.parseContents(root.node).psi, {item -> item is JSLiteralExpression && pattern.accepts(item)})
    }
}

/**
 * Utility function for collecting elements in Psi tree by preicate.
 * For cases when text gets parsed on the fly, it is impossible to use PsiTreeUtil, since it fails to check if elements are physical.
 */
fun collectChildren(element: PsiElement, predicate: (psiElement: PsiElement) -> Boolean): List<PsiElement> {
    val acc: MutableList<PsiElement> = mutableListOf()
    val maxDepth = 5
    fun inner(item: PsiElement, depth: Int) {
        //Check depth for safety:
        if (depth == maxDepth) return
        if (predicate (item)) {
            acc.add(item)
            //Won't go deeper:
            return
        } else {
            item.children.forEach { inner(it, depth+1) }
        }
    }
    inner(element, 0)
    return acc.toList()
}