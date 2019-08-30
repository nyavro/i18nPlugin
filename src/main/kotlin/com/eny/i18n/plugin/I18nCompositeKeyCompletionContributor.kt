package com.eny.i18n.plugin

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder

class I18nCompositeKeyCompletionContributor: CompletionContributor(), CompositeKeyResolver {

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
        val endsWithKeySeparator = text.endsWith(I18nFullKey.CompositeKeySeparator)
        val endsWithFileNameSeparator = text.endsWith(I18nFullKey.FileNameSeparator)
        val fixed = if (endsWithFileNameSeparator || endsWithKeySeparator) {
            text.substring(0, text.length - 1)
        } else text
        val fullKey = I18nFullKey.parse(fixed)
        if (fullKey?.fileName != null) {
            val files = JsonSearchUtil(parameters.position.project).findFilesByName(fullKey.fileName)
            files.forEach {
                file -> listCompositeKeyVariants(fullKey.compositeKey, file, !endsWithKeySeparator).forEach {
                    key -> result.addElement(LookupElementBuilder.create(text + key))
                }
            }
        }
    }
}