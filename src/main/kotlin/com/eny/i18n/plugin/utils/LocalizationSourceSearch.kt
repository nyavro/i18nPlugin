package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.factory.LocalizationType
import com.eny.i18n.plugin.ide.settings.commonSettings
import com.eny.i18n.plugin.ide.settings.mainFactory
import com.eny.i18n.plugin.vue.vueSettings
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.FilenameIndex

/**
 * Describes localization source.
 * May be root of json, yaml file, js object
 */
data class LocalizationSource(
    val element: PsiElement, val name: String, val parent: String, val displayPath: String, val type: LocalizationType,
    val host: PsiElement? = null
)

val directParent = {file: PsiFile -> file.containingDirectory}

fun localizationSource(file: PsiFile, resolveParent: (file: PsiFile) -> PsiDirectory, localizationType:LocalizationType): LocalizationSource {
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
        localizationType
    )
}

/**
 * Provides search of localization files (json, yaml)
 */
class LocalizationSourceSearch(private val project: Project) {

    private val vueSettings = project.vueSettings()

    /**
     * Finds localization sources
     */
    fun findFilesByHost(fileNames: List<String>, host: PsiElement): List<LocalizationSource> =
        findSourcesInner(fileNames, host, true)

    /**
     * Finds json roots by json file name
     */
    fun findSources(fileNames: List<String>, element: PsiElement? = null): List<LocalizationSource> =
        findSourcesInner(fileNames, element, false)

    /**
     * Finds json roots by json file name
     */
    private fun findSourcesInner(fileNames: List<String>, element: PsiElement? = null, isHost: Boolean): List<LocalizationSource> =
        project.mainFactory().localizationSourcesProviders()
            .filter {
                //Temporary compatibility fix
                (vueSettings.vue && it.javaClass.name.contains("Vue")) ||
                (!vueSettings.vue && !it.javaClass.name.contains("Vue"))
            }
            .flatMap {
                it.find(fileNames, element, isHost, project)
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
}

class SearchUtility(private val project: Project) {
    private val config = project.commonSettings()
    val translationFileTypes = project.mainFactory().contentGenerators().flatMap {it.getType().fileTypes}

    private fun findPsiRoot(virtualFile: VirtualFile, project: Project):PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)

    fun findSourcesByNames(fileNames: List<String>): List<LocalizationSource> =
        project.mainFactory().contentGenerators().flatMap { cg ->
            cg.getType().fileTypes.flatMap { ft ->
                findVirtualFilesByNames(fileNames, ft).mapNotNull { vf ->
                    findPsiRoot(vf, project)?.let {
                        localizationSource(it, directParent, cg.getType())
                    }
                }
            }
        }

    private fun findVirtualFilesByNames(fileNames: List<String>, fileType: FileType): List<VirtualFile> {
        val ext = fileType.defaultExtension
        return FileTypeIndex
            .getFiles(
                fileType,
                config.searchScope(project)
            )
            .filter { file -> fileNames.find {"$it.$ext" == file.name}.toBoolean()}
    }

    fun findVirtualFilesUnder(directory: String): List<PsiFile> =
        FilenameIndex.getFilesByName(project, directory, config.searchScope(project), true).toList().flatMap {
            it.children.toList().mapNotNull { root -> root.containingFile}
        }
}