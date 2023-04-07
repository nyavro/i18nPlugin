package com.eny.i18n.extensions.technology.i18next

import com.eny.i18n.*
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.whenMatches
import com.intellij.lang.javascript.TypeScriptFileType
import com.intellij.lang.javascript.psi.JSExpression
import com.intellij.lang.javascript.psi.JSObjectLiteralExpression
import com.intellij.lang.javascript.psi.JSProperty
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.TextOccurenceProcessor
import com.intellij.psi.search.UsageSearchContext
import com.intellij.psi.util.PsiTreeUtil

class I18NextTechnology : Technology {

    private val MAX_RESOLVE_DEPTH = 10

    private var cfgNamespaces: List<String> = listOf()

    override fun translationFunctions(): List<TranslationFunction> {
        return listOf(TranslationFunction("t", 0), TranslationFunction("useTranslation", 1))
    }

    override fun findSourcesByConfiguration(project: Project): List<LocalizationSource> {
        val searchScope = Settings.getInstance(project).config().searchScope(project)
        val localizations = Extensions.LOCALIZATION.extensionList
        return cfgNamespaces
            .mapNotNull { cfgFile -> FileTypeIndex.getFiles(TypeScriptFileType.INSTANCE, searchScope).find {it.name == cfgFile} }
            .flatMap {
                val init = PsiManager.getInstance(project).findFile(it)?.let { findInitObject(it) }
                val translations = getTranslations(
                    init?.let {listNamespaces(it)} ?: listOf()
                )
                translations.mapNotNull {
                    (name, expr) -> resolveReferenceExpression(expr, MAX_RESOLVE_DEPTH)?.let {Pair(name,it)}
                }.flatMap {
                    (name, expr) ->
                        localizations.filter {
                            it.matches(LocalizationFileType(expr.containingFile.fileType), null, listOf())
                        }.map {
                            expr.containingFile.putUserData(SOURCE_ROOT, true)
                            LocalizationSource(it.elementsTree(expr), name, expr.containingFile.name, expr.containingFile.containingDirectory.name, it)
                        }
                }
            }
    }

    override fun initialize(project: Project) {
        val config = Settings.getInstance(project).config()
        val searchScope = config.searchScope(project)
        val cfgFileNames = mutableListOf<String>()
        if(PsiSearchHelper.SearchCostResult.FEW_OCCURRENCES == PsiSearchHelper.getInstance(project).isCheapEnoughToSearch("translation", searchScope, null, null)) {
            val processor = object : TextOccurenceProcessor {
                override fun execute(element: PsiElement, offsetInElement: Int): Boolean {
                    (element.parent as? JSProperty)?.whenMatches {it.name == "translation"}?.containingFile?.also {
                        it.putUserData(SOURCE_ROOT, true)
                        cfgFileNames.add(it.name)
                    }
                    return false
                }
            }
            PsiSearchHelper.getInstance(project).processElementsWithWord(
                processor, searchScope, "translation", UsageSearchContext.IN_CODE, true
            )
        }
        this.cfgNamespaces = cfgFileNames.toList()
    }

    override fun cfgNamespaces(): List<String> = cfgNamespaces

    private fun findInitObject(root: PsiElement): JSObjectLiteralExpression? {
        return PsiTreeUtil.findFirstParent(PsiTreeUtil.findChildrenOfType(root, JSProperty::class.java).filter {
            it.name == "resources" && it.value is JSObjectLiteralExpression
        }.first()) {it is JSObjectLiteralExpression}?.let {it as JSObjectLiteralExpression}
    }

    private fun listNamespaces(init: JSObjectLiteralExpression): List<JSProperty> {
        return init.findProperty("resources")?.value?.let {it as? JSObjectLiteralExpression}?.properties?.toList() ?: listOf()
    }

    private fun getTranslations(namespaces: List<JSProperty>): List<Pair<String, JSExpression>> {
        return namespaces.filter { it.value is JSObjectLiteralExpression }.mapNotNull (::getTranslation)
    }

    private fun getTranslation(element: JSProperty): Pair<String, JSExpression>? {
        return element.name?.let {
            name -> (element.value as? JSObjectLiteralExpression)?.findProperty("translation")?.value?.let {Pair(name, it)}
        }
    }

    private tailrec fun resolveReferenceExpression(expr: PsiElement?, maxDepth: Int): PsiElement? {
        if(maxDepth == 0) return null
        return if(!(expr is PsiReference)) expr else resolveReferenceExpression(expr.resolve(), maxDepth-1)
    }
}
