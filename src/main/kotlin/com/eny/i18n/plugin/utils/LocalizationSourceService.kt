package com.eny.i18n.plugin.utils

import com.eny.i18n.Extensions
import com.eny.i18n.Localization
import com.eny.i18n.LocalizationSource
import com.eny.i18n.plugin.factory.LocalizationType
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex

@Service
class LocalizationSourceService {

    fun findSources(allNamespaces: List<String>, project: Project): List<LocalizationSource> {
        return Extensions.LOCALIZATION_SOURCE_PROVIDER.extensionList.flatMap{it.findLocalizationSources(project, allNamespaces)} +
                findLocalizationSources(project, allNamespaces)
    }

    private fun findLocalizationSources(project: Project, fileNames: List<String>): List<LocalizationSource> {
        return findVirtualFilesByName(project, fileNames.whenMatches { it.isNotEmpty() } ?: Settings.getInstance(project).config().defaultNamespaces())
            .flatMap { vf -> listOfNotNull(findPsiRoot(project, vf)).map {localizationSource(it){it.containingDirectory}}}
    }

    private fun localizationSource(file: PsiFile, resolveParent: (file: PsiFile) -> PsiDirectory): LocalizationSource {
        val parentDirectory = resolveParent(file)
        return LocalizationSource(
            file,
            file.name,
            parentDirectory.name,
            pathToRoot(
                file.project.basePath ?: "",
                file.containingDirectory
                    .virtualFile
                    .path
            ).trim('/') + '/' + file.name,
            LocalizationType(file.fileType)
        )
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
    private fun findVirtualFilesByName(project: Project, fileNames: List<String>): List<VirtualFile> {
        val searchScope = Settings.getInstance(project).config().searchScope(project)
        val flatMap = Extensions.LOCALIZATION.extensionList
            .flatMap(Localization::types)
        return flatMap
            .flatMap { localizationType ->
                FileTypeIndex
                    .getFiles(localizationType.languageFileType, searchScope)
                    .filter { file ->
                        fileNames.any { fileName -> (listOf(localizationType.languageFileType.defaultExtension) + localizationType.auxExtensions).any { ext -> "$fileName.$ext"==file.name}}
                    }
            }
    }

    //    Finds root of virtual file
    private fun findPsiRoot(project: Project, virtualFile: VirtualFile): PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
}