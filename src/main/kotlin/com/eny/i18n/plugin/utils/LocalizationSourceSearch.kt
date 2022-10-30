package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.factory.LocalizationType
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.json.*
import com.intellij.json.json5.Json5FileType
import com.intellij.json.psi.JsonObject
import com.intellij.lang.impl.PsiBuilderFactoryImpl
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.html.HtmlTag
import com.intellij.psi.impl.source.resolve.FileContextUtil.INJECTED_IN_ELEMENT
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlText
import org.jetbrains.yaml.YAMLFileType

/**
 * Describes localization source.
 * May be root of json, yaml file, js object
 */
data class LocalizationSource(
    val element: PsiElement, val name: String, val parent: String, val displayPath: String, val type: LocalizationType,
    val host: PsiElement? = null
)
/**
 * Provides search of localization files (json, yaml)
 */
class LocalizationSourceSearch(private val project: Project) {

    private val translationFileTypes =
        mapOf(
            Pair(JsonFileType.INSTANCE, emptyList()),
            Pair(Json5FileType.INSTANCE, emptyList()),
            Pair(YAMLFileType.YML, listOf("yaml"))
        )
    private val config = Settings.getInstance(project).config()

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

    private val directParent = {file: PsiFile -> file.containingDirectory}
    /**
     * Finds json roots by json file name
     */
    private fun findSourcesInner(fileNames: List<String>, element: PsiElement? = null, isHost: Boolean): List<LocalizationSource> =
        findPlainObjectFiles() +
        findVirtualFilesByName(fileNames.whenMatches { it.isNotEmpty() } ?: config.defaultNamespaces())
            .flatMap { vf -> listOfNotNull(findPsiRoot(vf)).map {localizationSource(it, directParent)}} +
        listOf()

    private fun findSfcSources(isHost: Boolean, element: PsiElement?): Iterable<LocalizationSource> {
        return PsiTreeUtil
            .findChildrenOfType(if (isHost) element?.containingFile else element?.containingFile?.getUserData(INJECTED_IN_ELEMENT)?.containingFile, HtmlTag::class.java)
            .toList()
            .find { it.name == "i18n" }
            ?.let { sfcSrcTag ->
                PsiTreeUtil.findChildOfType(sfcSrcTag, XmlText::class.java)?.let { sfcSourceText ->
                    val fileName = sfcSourceText.containingFile.name
                    JsonParser()
                        .parse(
                            JsonElementTypes.OBJECT,
                            PsiBuilderFactoryImpl().createBuilder(JsonParserDefinition(), JsonLexer(), sfcSourceText.text)
                        ).psi?.maybe<PsiElement, JsonObject>()
                        ?.propertyList
                        ?.map {
                            LocalizationSource(
                                it.value!!,
                                it.name,
                                fileName,
                                "SFC: ${fileName}/${it.name} ",
                                LocalizationType(JsonFileType.INSTANCE, "vue-sfc"),
                                sfcSourceText
                            )
                        }
                }
            }.fromNullable()
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
            LocalizationType(file.fileType, "general")
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

    private fun findVirtualFilesUnder(directory: String): List<PsiFile> =
        FilenameIndex.getFilesByName(project, directory, config.searchScope(project), true).toList().flatMap {
            it.children.toList().mapNotNull { root -> root.containingFile}
        }

    private fun findPlainObjectFiles(): List<LocalizationSource> {
        return findVirtualFilesUnder("LC_MESSAGES")
            .filter { it.virtualFile.extension == "po" }
            .map {
                localizationSource(it, { file: PsiFile ->
                    file.containingDirectory.parentDirectory ?: file.containingDirectory
                })
            }
    }

    private fun findVirtualFilesByName(fileNames: List<String>) =
        translationFileTypes.flatMap {findVirtualFilesByName(fileNames, it)}

//    Finds virtual files by names and type
    private fun findVirtualFilesByName(fileNames: List<String>, entry: Map.Entry<FileType, List<String>>): List<VirtualFile> {
        val exts = entry.value + entry.key.defaultExtension
        return FileTypeIndex
            .getFiles(
                entry.key,
                config.searchScope(project)
            )
            .filter { file -> fileNames.find { exts.any {ext -> "$it.$ext" == file.name}}.toBoolean()}
    }

//    Finds root of virtual file
    private fun findPsiRoot(virtualFile: VirtualFile):PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
}
