package com.eny.i18n.extensions.technology.i18next

import com.eny.i18n.Extensions
import com.eny.i18n.LocalizationFileType
import com.eny.i18n.LocalizationSource
import com.eny.i18n.Technology
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.lang.javascript.TypeScriptFileType
import com.intellij.lang.javascript.psi.JSExpression
import com.intellij.lang.javascript.psi.JSObjectLiteralExpression
import com.intellij.lang.javascript.psi.JSProperty
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.util.PsiTreeUtil

class I18NextTechnology : Technology {

    private val MAX_RESOLVE_DEPTH = 10

    override fun translationFunctionNames(): List<String> {
        return listOf("t")
    }

    override fun findSourcesByConfiguration(project: Project): List<LocalizationSource> {
        val searchScope = Settings.getInstance(project).config().searchScope(project)
        val localizations = Extensions.LOCALIZATION.extensionList
        FileTypeIndex.getFiles(TypeScriptFileType.INSTANCE, searchScope)
            .find {it.name == "i18n.ts"}
            ?.let {
                val init = PsiManager.getInstance(project).findFile(it)?.let { findInitObject(it) }
                val translations = getTranslations(
                    init?.let {listNamespaces(it)} ?: listOf()
                )
                val resolved = translations.mapNotNull {
                    (name, expr) -> resolveReferenceExpression(expr, MAX_RESOLVE_DEPTH)?.let {Pair(name,it)}
                }.flatMap {
                    (name, expr) ->
                        localizations.map {
                            it.matches(LocalizationFileType(expr.containingFile.fileType), null, listOf())
                        }
                }
            }
        return listOf()
    }

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
