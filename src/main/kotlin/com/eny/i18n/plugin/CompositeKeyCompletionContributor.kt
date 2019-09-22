package com.eny.i18n.plugin

import com.eny.i18n.plugin.utils.ExpressionKeyParser
import com.eny.i18n.plugin.utils.JsonSearchUtil
import com.eny.i18n.plugin.utils.Literal
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil

class CompositeKeyCompletionContributor: CompletionContributor() {

    private val CompositeKeySeparator = "."
    private val NsSeparator = ":"

    private val parser = ExpressionKeyParser()

    fun String.unQuote(): String {
        return listOf('\'', '\"', '`').fold(this) {
            acc, quote -> if (acc.endsWith(quote) && acc.startsWith(quote)) {
                return acc.substring(1, this.length - 1)
            } else acc
        }
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, result)
        val text = parameters.position.text.unQuote().substringBefore(CompletionInitializationContext.DUMMY_IDENTIFIER)
        val endsWithKeySeparator = text.endsWith(CompositeKeySeparator)
        val endsWithFileNameSeparator = text.endsWith(NsSeparator)
        val fixed = if (endsWithFileNameSeparator || endsWithKeySeparator) {
            text.substring(0, text.length - 1)
        } else text
        val fullKey = parser.parseLiteral(fixed)
        if (fullKey?.ns != null) {
            val files = JsonSearchUtil(parameters.position.project).findFilesByName(fullKey.ns.text)
            files.forEach {
                file -> listCompositeKeyVariants(fullKey.compositeKey, file, !endsWithKeySeparator).forEach {
                    key -> result.addElement(LookupElementBuilder.create(text + key))
                }
            }
        }
    }

    /**
     * Returns PsiElement by composite key from file's root node
     */
    private fun resolveCompositeKeyProperty(compositeKey: List<Literal>, fileNode: PsiElement): PsiElement? {
        val root: PsiElement? = PsiTreeUtil.getChildOfType(fileNode, JsonObject::class.java)
        return compositeKey.fold(root) {
            node, key -> if (node != null && node is JsonObject) node.findProperty(key.text)?.value else node
        }
    }

    /**
     * Returns keys at current composite key position
     */
    private fun listCompositeKeyVariants(compositeKey: List<Literal>, fileNode: PsiElement, substringSearch: Boolean): List<Literal> {
        val searchPrefix = if (substringSearch) compositeKey.last().text else ""
        val fixedKey = if (substringSearch) {
            compositeKey.dropLast(1)
        } else compositeKey
        return resolveCompositeKeyProperty(fixedKey, fileNode)?.
                node?.
                getChildren(TokenSet.create(JsonElementTypes.PROPERTY))?.
                asList()?.
                map { node -> node.firstChildNode.text.unQuote()}?.
                filter { key -> key.startsWith(searchPrefix)}?.
                map { key -> Literal(key.substringAfter(searchPrefix), key.substringAfter(searchPrefix).length, 0) } ?:
        listOf()
    }
}