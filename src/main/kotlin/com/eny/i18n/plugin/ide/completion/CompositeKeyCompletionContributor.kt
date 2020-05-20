package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.parser.DummyContext
import com.eny.i18n.plugin.key.FullKeyExtractor
import com.eny.i18n.plugin.parser.KeyExtractorImpl
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.utils.LocalizationSourceSearch
import com.eny.i18n.plugin.utils.nullableToList
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement

/**
 * Completion of i18n key
 */
class CompositeKeyCompletionContributor: CompletionContributor(), CompositeKeyResolver<PsiElement> {
    private val DUMMY_KEY = CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED
    private val keyExtractor = FullKeyExtractor(DummyContext(), KeyExtractorImpl())

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, result)
        if(parameters.position.text.unQuote().substringAfter(DUMMY_KEY).trim().isNotBlank()) return
        keyExtractor.extractFullKey(parameters.position)?.let {
            fullKey ->
                result.addAllElements(processKey(fullKey, parameters))
                result.stopHere()
        }
    }

    private fun groupPlurals(completions: List<String>, pluralSeparator: String):List<String> =
        completions.groupBy {it.substringBeforeLast(pluralSeparator)}
            .entries.flatMap {
                entry -> if(entry.value.size == 3 && entry.value.containsAll(listOf(1,2,5).map{entry.key+pluralSeparator+it})) {
                listOf(entry.key)} else entry.value
            }

    private fun processKey(fullKey: FullKey, parameters: CompletionParameters): List<LookupElementBuilder> =
        fullKey.compositeKey.lastOrNull().nullableToList().flatMap { last ->
            val source = fullKey.source.replace(last.text, "")
            groupPlurals(
                LocalizationSourceSearch(parameters.position.project).findFilesByName(fullKey.ns?.text).flatMap {
                    listCompositeKeyVariants(
                        fullKey.compositeKey.dropLast(1),
                        PsiElementTree.create(it.element),
                        last.text.replace(DUMMY_KEY, "")
                    ).map { it.value().text.unQuote() }
                },
                Settings.getInstance(parameters.position.project).config().pluralSeparator
            ).map { LookupElementBuilder.create(source + it) }
        }
}