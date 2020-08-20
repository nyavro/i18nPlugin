package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.json.JsonFileType
import com.intellij.json.json5.Json5FileType
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.FilenameIndex
import org.jetbrains.yaml.YAMLFileType
import java.io.File

/**
 * Describes localization source.
 * May be root of json, yaml file, js object
 */
data class LocalizationSource(val element: PsiElement, val name: String, val parent: String, val displayPath: String, val type: FileType)
/**
 * Provides search of localization files (json, yaml)
 */
class LocalizationSourceSearch(private val project: Project) {

    private val translationFileTypes = listOf(JsonFileType.INSTANCE, Json5FileType.INSTANCE, YAMLFileType.YML)
    private val config = Settings.getInstance(project).config()

    /**
     * Finds json roots by json file name
     */
    fun findFilesByNames(fileNames: List<String>): List<LocalizationSource> =
        findVirtualFilesByName(fileNames.whenMatches {it.isNotEmpty()} ?: config.defaultNamespaces()).flatMap {vf -> listOfNotNull(findPsiRoot(vf)).map (::localizationSource)} +
            if (config.vue) {
                findVirtualFilesUnder(config.vueDirectory)
                    .filter {file -> translationFileTypes.any {file.fileType==it}}
                    .map (::localizationSource)
//                val index = vueTranslationFiles.find {it.name.matches("index\\.(js|ts)".toRegex())}
//                if (index == null) {
//                    if (settings.jsConfiguration.isNotBlank()) {
//                        resolveJsRootsFromI18nObject(findVirtualFilesByName("i18n", JavaScriptFileType.INSTANCE).map {findPsiRoot(it)}.firstOrNull())
//                    } else {
//                        vueTranslationFiles.map { LocalizationSource(it, it.name, it.containingDirectory.name) }
//                    }
//                } else {
//                    resolveJsRoots(index)
//                }
            }
            else listOf()

    private fun localizationSource(file: PsiFile): LocalizationSource =
        LocalizationSource(
            file,
            file.name,
            file.containingDirectory.name,
            pathToRoot(
            file.project.basePath ?: "",
            file
                .containingDirectory
                .virtualFile
                .path
            ).trim(File.separatorChar) + File.separator + file.name,
            file.fileType
        )

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

    private fun findVirtualFilesUnder(directory: String): List<PsiFile> =
        FilenameIndex.getFilesByName(project, directory, config.searchScope(project), true).toList().flatMap {
            it.children.toList().map {root -> root.containingFile}
        }


//    Finds virtual files by file name
    private fun findVirtualFilesByName(fileNames: List<String>) =
        translationFileTypes.flatMap {findVirtualFilesByName(fileNames, it)}

//    Finds virtual files by names and type
    private fun findVirtualFilesByName(fileNames: List<String>, fileType: FileType): List<VirtualFile> {
        val ext = fileType.defaultExtension
        return FileTypeIndex
            .getFiles(
                fileType,
                config.searchScope(project)
            )
            .filter { file -> fileNames.find {"$it.$ext" == file.name}.toBoolean()}
    }

//    Finds root of virtual file
    private fun findPsiRoot(virtualFile: VirtualFile):PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
}