package com.eny.i18n.plugin.ide.references.translation

import com.eny.i18n.Extensions
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.KeyComposer
import com.eny.i18n.plugin.tree.Separators
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.codeInsight.daemon.impl.DaemonProgressIndicator
import com.intellij.openapi.components.Service
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.Computable
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.UsageSearchContext
import java.util.Collections.synchronizedList
@Service
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
            config.defaultNamespaces() + Extensions.TECHNOLOGY.extensionList.flatMap {it.cfgNamespaces()},
            false,
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
        val languages = Extensions.LANG.extensionList
        if (entry.text.unQuote().startsWith(key)) {
            val entryRef = languages.stream().map {lang -> lang.resolveLiteral(entry)}.filter {it!=null}.findFirst()
            entryRef.ifPresent { res.add(it) }
        }
        true
    }

    /**
     * Returns collected entries
     */
    fun entries(): Collection<PsiElement> = res
}

/**
 * Reference to key usage for translation file
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
