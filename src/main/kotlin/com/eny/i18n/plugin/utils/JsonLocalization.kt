package com.eny.i18n.plugin.utils

import com.eny.i18n.ContentGenerator
import com.eny.i18n.Localization
import com.eny.i18n.LocalizationFileType
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.fasterxml.jackson.core.io.JsonStringEncoder
import com.intellij.json.JsonFileType
import com.intellij.json.JsonLanguage
import com.intellij.json.json5.Json5FileType
import com.intellij.json.psi.JsonElementGenerator
import com.intellij.json.psi.JsonObject
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiElement

class JsonLocalization : Localization {
    override fun types(): List<LocalizationFileType> = listOf(JsonFileType.INSTANCE, Json5FileType.INSTANCE).map { LocalizationFileType(it) }
    override fun contentGenerator(): ContentGenerator = JsonContentGenerator()
}

/**
 * Generates JSON translation content
 */
private class JsonContentGenerator: ContentGenerator {

    private val tabChar = "  "

    override fun generateContent(compositeKey: List<Literal>, value: String): String {
        val escapedValue = String(JsonStringEncoder.getInstance().quoteAsString(value))
        return compositeKey.foldRightIndexed("\"$escapedValue\"", { i, key, acc ->
            val tab = tabChar.repeat(i)
            "{\n$tabChar$tab\"${key.text}\": $acc\n$tab}"
        })
    }

    override fun getType(): FileType = JsonFileType.INSTANCE
    override fun getLanguage(): Language = JsonLanguage.INSTANCE
    override fun getDescription(): String = PluginBundle.getMessage("quickfix.create.json.translation.files")
    override fun isSuitable(element: PsiElement): Boolean = element is JsonObject
    override fun generateTranslationEntry(item: PsiElement, key: String, value: String) {
        val obj = item as JsonObject
        val generator = JsonElementGenerator(item.project)
        val keyValue = generator.createProperty(key, value)
        val props = obj.getPropertyList()
        if (props.isEmpty()) {
            obj.addAfter(keyValue, obj.findElementAt(0))
            return
        }
        val separator = generator.createComma()
        val (element, anchor) = if (Settings.getInstance(item.project).extractSorted) {
            val before = props.takeWhile {it.name < key}
            if (before.isEmpty()) {
                Pair(separator, obj.addBefore(keyValue, props.first()))
            } else {
                Pair(keyValue, obj.addAfter(separator, before.last()))
            }
        }
        else {
            Pair(keyValue, obj.addAfter(separator, props.last()))
        }
        obj.addAfter(element, anchor)
    }

    override fun generate(element: PsiElement, fullKey: FullKey, unresolved: List<Literal>, translationValue: String?) =
        generateTranslationEntry(
            element,
            unresolved.first().text,
            generateContent(unresolved.drop(1), translationValue ?: fullKey.source)
        )
}