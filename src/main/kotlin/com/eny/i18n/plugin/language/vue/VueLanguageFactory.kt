package com.eny.i18n.plugin.language.vue

import com.eny.i18n.plugin.factory.*
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.default
import com.eny.i18n.plugin.utils.toBoolean
import com.eny.i18n.plugin.utils.unQuote
import com.eny.i18n.plugin.utils.whenMatches
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.html.HtmlTag
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlText
import com.intellij.psi.xml.XmlToken
import com.intellij.util.ProcessingContext
import org.jetbrains.vuejs.lang.expr.VueJSLanguage
import org.jetbrains.vuejs.lang.expr.parser.VueJSEmbeddedExprTokenType
import org.jetbrains.vuejs.lang.html.VueFileType

/**
 * Vue language components factory
 */
class VueLanguageFactory: LanguageFactory {
    override fun translationExtractor(): TranslationExtractor = VueTranslationExtractor()
    override fun foldingProvider(): FoldingProvider = VueFoldingProvider()
    override fun callContext(): CallContext = VueCallContext()
    override fun referenceAssistant(): ReferenceAssistant = VueReferenceAssistant()
}

internal class VueTranslationExtractor: TranslationExtractor {
    override fun canExtract(element: PsiElement): Boolean =
        element.isVueJs() || element.isJs() || element.isVue()

    override fun isExtracted(element: PsiElement): Boolean =
        if (element.isVueJs() || element.isJs()) {
            JSPatterns.jsArgument("\$t", 0).accepts(element.parent)
        } else {
            element.isVue() && element.text.startsWith("\$t")
        }

    override fun text(element: PsiElement): String =
        getTextElement(element).text.unQuote()

    override fun textRange(element: PsiElement): TextRange =
        getTextElement(element).textRange

    override fun template(element: PsiElement): (argument: String) -> String =
        when {
            element.isVueTemplate() -> ({"this.\$t($it)"})
            element.isVue() -> ({"{{ \$t($it) }}"})
            else -> ({"\$t($it)"})
        }

    private fun getTextElement(element: PsiElement): PsiElement =
        if (element.isBorderToken()) {
            element.prevSibling
        } else {
            element.whenMatches { it.isVueText() }?.parent.default(element)
        }
    private fun PsiElement.isJs(): Boolean = this.language == JavascriptLanguage.INSTANCE
    private fun PsiElement.isVueJs(): Boolean = this.language == VueJSLanguage.INSTANCE
    private fun PsiElement.isVue():Boolean = this.containingFile.fileType == VueFileType.INSTANCE
    private fun PsiElement.isVueTemplate():Boolean = this.isVue() && PsiTreeUtil.findFirstParent(this, {it is HtmlTag && it.name == "script"}).toBoolean()
    private fun PsiElement.isVueText(): Boolean = (this is PsiWhiteSpace) || (this is XmlToken)
    private fun PsiElement.isBorderToken(): Boolean = this.text == "</"
}

internal class VueFoldingProvider: FoldingProvider {
    override fun collectContainers(root: PsiElement): List<PsiElement> =
        PsiTreeUtil
            .findChildrenOfType(root, XmlText::class.java)
            .filter { PlatformPatterns.psiElement(XmlText::class.java).with(TemplateText()).accepts(it)}

    override fun collectLiterals(container: PsiElement): Pair<List<PsiElement>, Int> {
        val vueParser = VueJSEmbeddedExprTokenType.createInterpolationExpression(container.project)
        val copy = trimLeft(unwrapTemplate(container))
        return Pair(
            collectChildren(vueParser.parseContents(copy.node).psi) { item -> item is JSLiteralExpression && JSPatterns.jsArgument("\$t", 0).accepts(item)},
            container.text.indexOf(copy.text)
        )
    }

    override fun getFoldingRange(container: PsiElement, offset: Int, psiElement: PsiElement): TextRange =
        PsiTreeUtil
            .getParentOfType(psiElement, JSCallExpression::class.java)
            .default(psiElement)
            .textRange
            .shiftRight(container.textOffset + offset)

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
}

internal class VueCallContext: CallContext {
    override fun accepts(element: PsiElement): Boolean =
        listOf("JS:STRING_LITERAL", "JS:LITERAL_EXPRESSION", "JS:STRING_TEMPLATE_PART", "JS:STRING_TEMPLATE_EXPRESSION")
            .contains(element.type()) &&
            JSPatterns.jsArgument("\$t", 0).let { pattern ->
                pattern.accepts(element) ||
                        pattern.accepts(PsiTreeUtil.findFirstParent(element, { it.parent?.type() == "JS:ARGUMENT_LIST" }))
            }
}

internal class VueReferenceAssistant: ReferenceAssistant {

    override fun pattern(): ElementPattern<out PsiElement> = JSPatterns.jsLiteralExpression()

    override fun extractKey(element: PsiElement): FullKey? {
        val settings = Settings.getInstance(element.project)
        return null
//        listOf(StringLiteralKeyExtractor())
//                .find {it.canExtract(element)}
//                ?.let{parser.parse(it.extract(element), settings.nsSeparator, settings.keySeparator)}
    }
}