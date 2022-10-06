package com.eny.i18n.plugin.ide.references.translation

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.KeyComposer
import com.eny.i18n.plugin.tree.Separators
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.UsageSearchContext
import java.util.Collections.synchronizedList
import org.bouncycastle.crypto.params.Blake3Parameters.context

import com.intellij.codeInsight.daemon.impl.DaemonProgressIndicator
import com.intellij.openapi.util.Computable
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiDocumentManager

import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerEx

import com.intellij.codeInsight.daemon.impl.HighlightInfo





internal class TranslationToCodeReferenceProvider : KeyComposer<PsiElement> {

    /**
     * @param element PsiElement to get references of.
     * @param textRange TextRange to highlight
     */
    fun getReferences(element: PsiElement, textRange: TextRange, parents: List<String>): List<PsiReference> {
        val project = element.project
        val config = Settings.getInstance(project).config()
        val key = composeKey(
            parents,
            Separators(config.nsSeparator, config.keySeparator, config.pluralSeparator),
            config.defaultNamespaces(),
            config.vue && element.containingFile.parent?.name == config.vueDirectory,
            config.firstComponentNs
        )
        if (PsiSearchHelper.SearchCostResult.FEW_OCCURRENCES ==
                PsiSearchHelper
                    .getInstance(project)
                    .isCheapEnoughToSearch(
                        key,
                        config.searchScope(project),
                        null,
                        null
                    )
        ) {
            return listOf(TranslationToCodeReference(element, textRange, key))
        }
        return emptyList()
    }
}

/**
 * Accumulates references
 */
class ReferencesAccumulator(private val key: String) {

    private val res = synchronizedList(mutableListOf<PsiElement>())

    /**
     * Processing function for PsiSearchHelper
     */
    fun process() = {
        entry: PsiElement, _:Int ->
//        if(indicator.isCanceled) {
//            indicator.checkCanceled() // maybe it'll throw with some useful additional information
//            throw ProcessCanceledException()
//        }
        val typeName = entry.node.elementType.toString()
        if (entry.text.unQuote().startsWith(key)) {
            if (listOf("JS:STRING_LITERAL", "quoted string", "JS:STRING_TEMPLATE_EXPRESSION").any { typeName.contains(it) }) {
                res.add(entry)
            } else if (typeName == "JS:STRING_TEMPLATE_PART") {
                res.add(entry.parent)
            }
        }
        true
    }

    /**
     * Returns collected entries
     */
    fun entries(): Collection<PsiElement> = res
}

/**
 * Reference to key usage for json translation file
 */
class TranslationToCodeReference(element: PsiElement, textRange: TextRange, val composedKey: String) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    /**
     * Finds usages of json translation
     */
    fun findRefs(): Collection<PsiElement> {
        return ProgressManager.getInstance().runProcess (
            Computable {
                val project = element.project
                val referencesAccumulator = ReferencesAccumulator(composedKey)
                PsiSearchHelper.getInstance(project).processElementsWithWord(
                    referencesAccumulator.process(),
                    Settings.getInstance(project).config().searchScope(project),
                    composedKey,
                    UsageSearchContext.ANY,
                    true
                )
                referencesAccumulator.entries()
            },
            DaemonProgressIndicator()
        )
    }

    override fun resolve(): PsiElement? = multiResolve(false).firstOrNull()?.element

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
            findRefs()
                    .map {property -> PsiElementResolveResult(property) }
                    .toTypedArray()
}
