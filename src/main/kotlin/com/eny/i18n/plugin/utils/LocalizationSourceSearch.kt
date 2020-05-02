package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.json.JsonFileType
import com.intellij.lang.ecmascript6.psi.ES6ExportDefaultAssignment
import com.intellij.lang.ecmascript6.psi.ES6ImportedBinding
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.lang.javascript.psi.JSNewExpression
import com.intellij.lang.javascript.psi.JSObjectLiteralExpression
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.lang.javascript.psi.impl.JSObjectLiteralExpressionImpl
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.YAMLFileType

/**
 * Describes localization source.
 * May be root of json, yaml file, js object
 */
data class LocalizationSource(val element: PsiElement, val name: String, val parent: String)

/**
 * Provides search of localization files (json, yaml)
 */
class LocalizationSourceSearch(private val project: Project) {

    val settings = Settings.getInstance(project)

    /**
     * Finds json roots by json file name
     */
    fun findFilesByName(fileName: String?): List<LocalizationSource> =
        findVirtualFilesByName(fileName ?: settings.defaultNs).flatMap {vf -> listOfNotNull(findPsiRoot(vf)).map {LocalizationSource(it, it.name, it.containingDirectory.name)}} +
            if (settings.vue) {
                val vueTranslationFiles = findVirtualFilesUnder(settings.vueDirectory)
                val index = vueTranslationFiles.find {it.name.matches("index\\.(js|ts)".toRegex())}
                if (index == null) {
                    if (settings.jsConfiguration.isNotBlank()) {
                        resolveJsRootsFromI18nObject(findVirtualFilesByName("i18n", JavaScriptFileType.INSTANCE).map {findPsiRoot(it)}.firstOrNull())
                    } else {
                        vueTranslationFiles.map { LocalizationSource(it, it.name, it.containingDirectory.name) }
                    }
                } else {
                    resolveJsRoots(index)
                }
            }
            else listOf()

    private fun resolveJsRootsFromI18nObject(file: PsiFile?): List<LocalizationSource> {
        if (file == null) {
            return listOf()
        } else {
            val vueI18nObject = PsiTreeUtil.findChildOfType(
                PsiTreeUtil.findChildrenOfType(file, JSNewExpression::class.java).filter {
                    PsiTreeUtil.findChildOfType(it, JSReferenceExpression::class.java)?.text == "VueI18n"
                }.firstOrNull(),
                JSObjectLiteralExpression::class.java
            )
            return (vueI18nObject?.findProperty("messages")?.value as? JSObjectLiteralExpressionImpl)?.properties?.map {
                LocalizationSource(it.value!!, it.name!!, file.name)
            } ?: listOf()
        }
    }

    private fun resolveJsRoots(file: PsiFile): List<LocalizationSource> {
        return PsiTreeUtil
            .findChildOfType(PsiTreeUtil.findChildOfType(file, ES6ExportDefaultAssignment::class.java), JSObjectLiteralExpression::class.java)
            ?.properties
            ?.mapNotNull {
                PsiTreeUtil.findChildOfType(
                    (it.value?.reference?.resolve() as? ES6ImportedBinding)?.findReferencedElements()?.firstOrNull(),
                    JSObjectLiteralExpression::class.java
                )?.let {
                    value -> LocalizationSource(value, it.name!!, file.name)
                }
            } ?: listOf()
    }

    private fun findVirtualFilesUnder(directory: String): List<PsiFile> =
        FilenameIndex.getFilesByName(project, directory, settings.searchScope(project), true).toList().flatMap {
            item -> item.children.toList().mapNotNull {root -> root.containingFile}
        }


//    Finds virtual files by file name
    private fun findVirtualFilesByName(fileName: String) =
        findVirtualFilesByName(fileName, JsonFileType.INSTANCE) + findVirtualFilesByName(fileName, YAMLFileType.YML)

//    Finds virtual files by file name and type
    private fun findVirtualFilesByName(fileName: String, fileType: FileType): List<VirtualFile> {
        val ext = fileType.defaultExtension
        return FileTypeIndex
            .getFiles(
                fileType,
                if (settings.searchInProjectOnly) GlobalSearchScope.projectScope(project)
                else GlobalSearchScope.allScope(project)
            )
            .filter { file -> file.name == "$fileName.$ext" }
    }

//    Finds root of virtual file
    private fun findPsiRoot(virtualFile: VirtualFile):PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
}