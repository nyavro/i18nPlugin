package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.Literal
import com.intellij.json.JsonFileType
import com.intellij.json.JsonLanguage
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import org.jetbrains.yaml.YAMLFileType
import org.jetbrains.yaml.YAMLLanguage

/**
 * Localization file content generator
 */
interface ContentGenerator {

    /**
     * Generates content by given i18n key
     */
    fun generateContent(fullKey: FullKey, value: String?): String = generateContent(fullKey.compositeKey, value ?: "TODO-${fullKey.source}")


    /**
     * Generates content by given composite key
     */

    fun generateContent(compositeKey: List<Literal>, value: String): String

    /**
     * Returns file type
     */
    fun getFileType(): FileType

    /**
     * Returns language
     */
    fun getLanguage(): Language

    /**
     * Returns description
     */
    fun getDescription(): String
}

/**
 * Generates JSON translation content
 */
class JsonContentGenerator: ContentGenerator {
    override fun generateContent(compositeKey: List<Literal>, value: String): String =
        compositeKey.foldRightIndexed("\"$value\"", { i, key, acc ->
            val tab = "\t".repeat(i)
            "{\n\t$tab\"${key.text}\": $acc\n$tab}"
        })

    override fun getFileType(): FileType = JsonFileType.INSTANCE
    override fun getLanguage(): Language = JsonLanguage.INSTANCE
    override fun getDescription(): String = "Create json translation files"
}

/**
 * Generates YAML translation content
 */
class YamlContentGenerator: ContentGenerator {
    override fun generateContent(compositeKey: List<Literal>, value: String): String =
        compositeKey.foldRightIndexed(value, { i, key, acc ->
            val caret = if (i == 0) "" else "\n"
            val tab = "\t".repeat(i)
            "$caret$tab${key.text}: $acc"
        })

    override fun getFileType(): FileType = YAMLFileType.YML
    override fun getLanguage(): Language = YAMLLanguage.INSTANCE
    override fun getDescription(): String = "Create yaml translation files"
}