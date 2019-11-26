package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.parser.FullKeyExtractor
import com.eny.i18n.plugin.utils.JsonSearchUtil
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class CompositeKeyCompletionContributor: CompletionContributor(), CompositeKeyResolver<PsiElement> {
    private val jsUtil = FullKeyExtractor()

    private fun groupPlurals(set: Set<String>, pluralSeparator: String):List<String> =
        set
            .groupBy {it.substringBeforeLast(pluralSeparator)}
            .entries.flatMap {
            entry -> if(entry.value.size == 3 && entry.value.containsAll(listOf(1,2,5).map{entry.key+pluralSeparator+it})) {
            listOf(entry.key)} else entry.value
        }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, result)
        val settings = Settings.getInstance(parameters.position.project)
        val fullKey = jsUtil.extractI18nKeyLiteral(
            if (parameters.position.toString().contains("JS:STRING_TEMPLATE_PART")) parameters.position.parent
            else parameters.position
        )
        val compositeKey = fullKey?.compositeKey
        val fixedKey = compositeKey?.dropLast(1)
        val last = compositeKey?.last()
        val fixedPart = last?.text?.substringBefore(CompletionInitializationContext.DUMMY_IDENTIFIER)
        val fixedPart2 = last?.text?.substringAfter(CompletionInitializationContext.DUMMY_IDENTIFIER)
        val search = Regex(fixedPart + ".*" + fixedPart2)
        val source = fullKey?.source?.replace(fixedPart + CompletionInitializationContext.DUMMY_IDENTIFIER + fixedPart2, "")
        if (fullKey?.ns != null && fixedKey != null) {
            val elements =
                groupPlurals(
                    JsonSearchUtil(parameters.position.project).findFilesByName(fullKey.ns.text).flatMap {
                        file ->
                            listCompositeKeyVariants(
                                fixedKey,
                                PsiTreeUtil.getChildOfType(file, JsonObject::class.java)?.let{fileRoot -> PsiElementTree(fileRoot)},
                                search
                            )
                    }.map {key -> key.value().text.unQuote()}.toSet(),
                    settings.pluralSeparator
                )
            result.addAllElements(
                elements.map {item -> LookupElementBuilder.create(source + item)}
            )
        }
    }
}