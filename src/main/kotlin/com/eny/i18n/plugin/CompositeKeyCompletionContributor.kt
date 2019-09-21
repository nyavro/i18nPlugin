package com.eny.i18n.plugin

import com.eny.i18n.plugin.utils.CompositeKeyResolver
import com.eny.i18n.plugin.utils.ExpressionKeyParser
import com.eny.i18n.plugin.utils.JsonSearchUtil
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder

class CompositeKeyCompletionContributor: CompletionContributor(), CompositeKeyResolver {

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
}