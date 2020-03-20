package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.language.I18nLanguage
import com.eny.i18n.plugin.language.psi.I18nFullKey
import com.eny.i18n.plugin.language.psi.I18nKeyItem
import com.eny.i18n.plugin.language.psi.I18nRoot
import com.eny.i18n.plugin.language.psi.I18nShortKey
import com.eny.i18n.plugin.parser.FullKeyExtractor
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.Literal
import com.eny.i18n.plugin.utils.LocalizationFileSearch
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext


/**
 * Completion of i18n key
 */
class I18nCompletionContributor: CompletionContributor() {
    private val keyExtractor = FullKeyExtractor()

    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(I18nLanguage.Instance),
            I18nCompletionProvider()
        )
    }

    internal class I18nCompletionProvider : CompletionProvider<CompletionParameters>(), CompositeKeyResolver<PsiElement> {
        private val DUMMY_KEY = CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val element = parameters.position.parent
            if (element is I18nKeyItem) {
                if(element.text.unQuote().substringAfter(DUMMY_KEY).trim().isNotBlank()) return
                val i18nRoot = PsiTreeUtil.getParentOfType(element, I18nRoot::class.java)
                val child = i18nRoot?.firstChild
                val settings = Settings.getInstance(parameters.position.project)
                if (child is I18nShortKey) {
                    processShortKey(child)
                } else if (child is I18nFullKey) {
                    processFullKey(child, result, parameters, settings)
                }
                result.stopHere()
            }
//        keyExtractor.extractI18nKeyLiteral(element)?.let {
//            fullKey ->
//            if (fullKey.source.endsWith(".$DUMMY_KEY")) {
//                processKey(fullKey, result, parameters, settings)
//            } else {
//                processKey(endWithDot(fullKey), result, parameters, settings)
//            }
//            result.stopHere()
//        }
        }

        private fun processFullKey(fullKey: I18nFullKey, result: CompletionResultSet, parameters: CompletionParameters, settings: Settings) {
            processKey(fullKey, result, parameters, settings, true)
//            if (!fullKey.text.endsWith(settings.keySeparator)) {
//                processKey(fullKey, result, parameters, settings, true)
//            }
        }

        private fun processShortKey(child: I18nShortKey) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        private fun groupPlurals(set: Set<String>, pluralSeparator: String):List<String> =
            set.groupBy {it.substringBeforeLast(pluralSeparator)}
                .entries.flatMap {
                entry -> if(entry.value.size == 3 && entry.value.containsAll(listOf(1,2,5).map{entry.key+pluralSeparator+it})) {
                listOf(entry.key)} else entry.value
            }

        private fun endWithDot(fullKey: FullKey): FullKey = fullKey.copy(
            source = fullKey.source.replace(DUMMY_KEY, ".$DUMMY_KEY"),
            compositeKey =
            fullKey.compositeKey.dropLast(1) +
                fullKey.compositeKey.takeLast(1).map {key -> Literal(key.text.replace(DUMMY_KEY, "")) } +
                Literal(DUMMY_KEY)
        )

        private fun processKey(
            fullKey: I18nFullKey,
            result: CompletionResultSet,
            parameters: CompletionParameters,
            settings: Settings,
            forceAddSeparator: Boolean) {
            val handler = KeyInsertHandler(forceAddSeparator, settings)
            val compositeKey = fullKey.compositeKey.children.flatMap {
                item ->
                    val text = item.text
                    if (forceAddSeparator && text.endsWith(DUMMY_KEY)) {
                        listOf(text.substringBefore(DUMMY_KEY), DUMMY_KEY)
                    } else {
                        listOf(text)
                    }
            }
            val fixedKey = compositeKey.dropLast(1).map {
                item -> Literal(item)
            }
            compositeKey.lastOrNull()?.let { last ->
                val search = last.let { value -> Regex(value.replace(DUMMY_KEY, ".*")) }
                val groupPlurals = groupPlurals(
                    LocalizationFileSearch(parameters.position.project).findFilesByName(fullKey.namespace.text).flatMap { file ->
                        listCompositeKeyVariants(
                            fixedKey,
                            PsiElementTree.create(file),
                            search
                        )
                    }.map { key -> key.value().text.unQuote() }.toSet(),
                    settings.pluralSeparator
                )
                val map = groupPlurals.map { item ->
                    LookupElementBuilder.create(item).withInsertHandler(handler)
                }
                result.addAllElements(
                    map
                )
            }
        }
    }

    internal class KeyInsertHandler constructor(private val withSeparator: Boolean, private val settings: Settings) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            if (withSeparator) {
                val quoted = settings.keySeparator + item.lookupString
                context.document.replaceString(context.startOffset, context.tailOffset, quoted)
            }
        }
    }
}