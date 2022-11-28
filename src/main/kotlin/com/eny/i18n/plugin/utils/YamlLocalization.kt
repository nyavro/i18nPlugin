package com.eny.i18n.plugin.utils

import com.eny.i18n.ContentGenerator
import com.eny.i18n.Localization
import com.eny.i18n.LocalizationFileType
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.YAMLElementGenerator
import org.jetbrains.yaml.YAMLFileType
import org.jetbrains.yaml.YAMLLanguage
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLMapping

class YamlLocalization : Localization {
    override fun types(): List<LocalizationFileType> = listOf(LocalizationFileType(YAMLFileType.YML, listOf("yaml")))
    override fun contentGenerator(): ContentGenerator = YamlContentGenerator()
}

private class YamlContentGenerator: ContentGenerator {

    private val tabChar = "  "

    override fun generateContent(compositeKey: List<Literal>, value: String): String =
        compositeKey.foldRightIndexed(value, { i, key, acc ->
            val caret = if (i == 0) "" else "\n"
            val tab = tabChar.repeat(i)
            "$caret$tab${key.text}: $acc"
        })

    override fun getType(): FileType = YAMLFileType.YML
    override fun getLanguage(): Language = YAMLLanguage.INSTANCE
    override fun getDescription(): String = PluginBundle.getMessage("quickfix.create.yaml.translation.files")
    override fun isSuitable(element: PsiElement): Boolean = (element is YAMLMapping) || (element is YAMLDocument)
    override fun generateTranslationEntry(item: PsiElement, key: String, value: String) {
        val generator = YAMLElementGenerator.getInstance(item.project)
        val keyValue = generator.createYamlKeyValue(key, value)
        if (item is YAMLDocument) {
            item.add(keyValue)
            return
        }
        val obj = (item as YAMLMapping)
        val props = obj.keyValues
        val separator = generator.createEol()
        val (element, anchor) = if (Settings.getInstance(item.project).extractSorted) {
            val before = props.takeWhile {it.name ?: "" < key}
            if (before.isEmpty()) {
                Pair(separator, obj.addBefore(keyValue, if (props.isEmpty()) item.node.firstChildNode.psi else props.first()))
            } else {
                Pair(keyValue, obj.addAfter(separator, before.last()))
            }
        } else {
            Pair(keyValue, obj.addAfter(separator, if (props.isEmpty()) item.node.firstChildNode.psi else props.last()))
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