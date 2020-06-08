package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.factory.CallContext
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.FullKeyExtractor
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.parser.DummyContext
import com.eny.i18n.plugin.parser.KeyExtractorImpl
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.LocalizationSourceSearch
import com.eny.i18n.plugin.utils.nullableToList
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

/**
 * Completion of i18n key
 */
abstract class CompositeKeyCompletionContributor(private val callContext: CallContext): CompletionContributor(), CompositeKeyResolver<PsiElement> {
    private val DUMMY_KEY = CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED
    private val keyExtractor = FullKeyExtractor(DummyContext(), KeyExtractorImpl())

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
//        super.fillCompletionVariants(parameters, result)
        if(parameters.position.text.unQuote().substringAfter(DUMMY_KEY).trim().isNotBlank()) return
        val isInTranslationContext = callContext.accepts(parameters.position.parent)
        val fullKey = keyExtractor.extractFullKey(parameters.position)
        if (fullKey == null) {
            if (isInTranslationContext) {
                val prefix = parameters.position.text.replace(DUMMY_KEY, "").unQuote().trim()
                val emptyKeyCompletions = emptyKeyCompletions(parameters.position.project, prefix)
                result.addAllElements(emptyKeyCompletions)
                result.stopHere()
            }
        } else {
            val processKey = processKey(fullKey, parameters)
            result.addAllElements(processKey)
            result.stopHere()
        }
    }

    private fun emptyKeyCompletions(project: Project, prefix: String): List<LookupElementBuilder> = findCompletions(
        project, prefix, "", null, emptyList()
    )

    private fun groupPlurals(completions: List<String>, pluralSeparator: String):List<String> =
        completions.groupBy {it.substringBeforeLast(pluralSeparator)}
            .entries.flatMap {
                entry -> if(entry.value.size == 3 && entry.value.containsAll(listOf(1,2,5).map{entry.key+pluralSeparator+it})) {
                listOf(entry.key)} else entry.value
            }

    private fun processKey(fullKey: FullKey, parameters: CompletionParameters): List<LookupElementBuilder> =
        fullKey.compositeKey.lastOrNull().nullableToList().flatMap { last ->
            val source = fullKey.source.replace(last.text, "")
            val prefix = last.text.replace(DUMMY_KEY, "")
            findCompletions(parameters.position.project, prefix, source, fullKey.ns?.text, fullKey.compositeKey.dropLast(1))
        }

    private fun findCompletions(project: Project, prefix: String, source: String, ns: String?, compositeKey: List<Literal>): List<LookupElementBuilder> {
        return groupPlurals(
            LocalizationSourceSearch(project).findFilesByName(ns).flatMap {
                listCompositeKeyVariants(
                    compositeKey,
                    PsiElementTree.create(it.element),
                    prefix
                ).map { it.value().text.unQuote() }
            },
            Settings.getInstance(project).config().pluralSeparator
        ).map { LookupElementBuilder.create(source + it) }
    }
}