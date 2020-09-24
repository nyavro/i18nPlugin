package com.eny.i18n.plugin.localization.yaml

import com.eny.i18n.plugin.factory.ContentGenerator
import com.eny.i18n.plugin.factory.LocalizationFactory
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.utils.PluginBundle
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.YAMLElementGenerator
import org.jetbrains.yaml.YAMLFileType
import org.jetbrains.yaml.YAMLLanguage
import org.jetbrains.yaml.psi.YAMLMapping

private val tabChar = "  "

class YamlLocalizationFactory: LocalizationFactory {
    override fun contentGenerator(): ContentGenerator = YamlContentGenerator()
}

/**
 * Generates YAML translation content
 */
class YamlContentGenerator: ContentGenerator {
    override fun generateContent(compositeKey: List<Literal>, value: String): String =
        compositeKey.foldRightIndexed(value, { i, key, acc ->
            val caret = if (i == 0) "" else "\n"
            val tab = tabChar.repeat(i)
            "$caret$tab${key.text}: $acc"
        })

    override fun getFileType(): FileType = YAMLFileType.YML
    override fun getLanguage(): Language = YAMLLanguage.INSTANCE
    override fun getDescription(): String = PluginBundle.getMessage("quickfix.create.yaml.translation.files")
    override fun isSuitable(element: PsiElement): Boolean = element is YAMLMapping
    override fun generateTranslationEntry(element: PsiElement, key: String, value: String) {
        val generator = YAMLElementGenerator.getInstance(element.project)
        if (Settings.getInstance(element.project).extractSorted) {
            val yamlObject = (element as YAMLMapping)
            val props = yamlObject.keyValues
            val before = props.takeWhile {it.name ?: "" < key}
            if (before.isEmpty()) {
                yamlObject.addAfter(
                    generator.createEol(),
                    yamlObject.addBefore(generator.createYamlKeyValue(key, value), props.first())
                )
            }
        } else {
            element.add(generator.createEol())
            element.add(generator.createYamlKeyValue(key, value))
        }
    }
    override fun generate(element: PsiElement, fullKey: FullKey, unresolved: List<Literal>, translationValue: String?) =
        generateTranslationEntry(
            element,
            unresolved.first().text,
            generateContent(unresolved.drop(1), translationValue ?: fullKey.source)
        )
}