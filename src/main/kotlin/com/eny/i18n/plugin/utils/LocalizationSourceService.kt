package com.eny.i18n.plugin.utils

import com.eny.i18n.Extensions
import com.eny.i18n.Localization
import com.eny.i18n.LocalizationSource
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex

@Service
class LocalizationSourceService {

    fun findSources(fileNames: List<String>, project: Project): List<LocalizationSource> {
        return findVirtualFilesByName(project, fileNames.whenMatches { it.isNotEmpty() } ?: Settings.getInstance(project).config().defaultNamespaces())
    }

//    private fun resolveJsRootsFromI18nObject(file: PsiFile?): List<LocalizationSource> {
//        if (file == null) {
//            return listOf()
//        } else {
//            val vueI18nObject = PsiTreeUtil.findChildOfType(
//                PsiTreeUtil.findChildrenOfType(file, JSNewExpression::class.java).firstOrNull {
//                    PsiTreeUtil.findChildOfType(it, JSReferenceExpression::class.java)?.text == "VueI18n"
//                },
//                JSObjectLiteralExpression::class.java
//            )
//            return (vueI18nObject?.findProperty("messages")?.value as? JSObjectLiteralExpressionImpl)?.properties?.map {
//                LocalizationSource(it.value!!, it.name!!, file.name)
//            } ?: listOf()
//        }
//    }

//    private fun resolveJsRoots(file: PsiFile): List<LocalizationSource> {
//        return PsiTreeUtil
//            .findChildOfType(PsiTreeUtil.findChildOfType(file, ES6ExportDefaultAssignment::class.java), JSObjectLiteralExpression::class.java)
//            ?.properties
//            ?.mapNotNull {
//                PsiTreeUtil.findChildOfType(
//                    (it.value?.reference?.resolve() as? ES6ImportedBinding)?.findReferencedElements()?.firstOrNull(),
//                    JSObjectLiteralExpression::class.java
//                )?.let {
//                    value -> LocalizationSource(value, it.name!!, file.name)
//                }
//            } ?: listOf()
//    }

    //    Finds virtual files by names and type
    private fun findVirtualFilesByName(project: Project, fileNames: List<String>): List<LocalizationSource> {
        return Extensions.LOCALIZATION.extensionList.flatMap {findSourcesByFileType(project, fileNames, it)}
    }

    private fun findSourcesByFileType(project: Project, fileNames: List<String>, localization: Localization<PsiElement>): List<LocalizationSource> {
        val searchScope = Settings.getInstance(project).config().searchScope(project)
        return localization.types().flatMap { localizationType ->
            FileTypeIndex
                .getFiles(localizationType.languageFileType, searchScope)
                .filter { file -> localization.matches(localizationType, file, fileNames) }
                .mapNotNull { virtualFile ->
                    PsiManager.getInstance(project).findFile(virtualFile)?.let { file ->
                        LocalizationSource(
                            localization.elementsTree(file),
                            file.name,
                            file.containingDirectory.name,
                            pathToRoot(
                                file.project.basePath ?: "",
                                file.containingDirectory
                                    .virtualFile
                                    .path
                            ).trim('/') + '/' + file.name,
                            localization
                        )
                    }
                }
        }
    }
}