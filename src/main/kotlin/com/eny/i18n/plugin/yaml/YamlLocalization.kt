package com.eny.i18n.plugin.yaml

import com.eny.i18n.plugin.factory.ContentGenerator
import com.eny.i18n.plugin.factory.LocalizationFactory
import com.eny.i18n.plugin.factory.LocalizationType
import com.eny.i18n.plugin.factory.TranslationReferenceAssistant
import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReferenceProvider
import com.eny.i18n.plugin.ide.settings.commonSettings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.CollectingSequence
import com.eny.i18n.plugin.utils.PluginBundle
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parents
import org.jetbrains.yaml.YAMLElementGenerator
import org.jetbrains.yaml.YAMLFileType
import org.jetbrains.yaml.YAMLLanguage
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

private val tabChar = "  "

class YamlLocalizationFactory: LocalizationFactory {
    override fun elementTreeFactory(): (file: PsiElement) -> PsiElementTree? = {
        (it as? YAMLFile)?.let {YamlElementTree.create(it)}
    }
    override fun contentGenerator(): ContentGenerator = YamlContentGenerator()
    override fun referenceAssistant(): TranslationReferenceAssistant<YAMLKeyValue> = YamlReferenceAssistant()
}

private class YamlReferenceAssistant : TranslationReferenceAssistant<YAMLKeyValue> {

    private val provider = TranslationToCodeReferenceProvider()

    override fun pattern(): ElementPattern<out YAMLKeyValue> = PlatformPatterns.psiElement(YAMLKeyValue::class.java)

    private fun parents(element: YAMLKeyValue): List<String> =
        CollectingSequence(element.parents()) {
            when {
                it is YAMLKeyValue -> it.key!!.text.unQuote()
                it is YAMLFile -> it.name.substringBeforeLast(".")
                else -> null
            }
        }.toList().reversed()

    private fun textRange(element: YAMLKeyValue): TextRange {
        val text = element.key!!.text
        return TextRange(if (text.startsWith("\"")) 1 else 0, text.length - (if (text.endsWith("\"")) 1 else 0))
    }

    override fun references(element: YAMLKeyValue): List<PsiReference> =
        provider.getReferences(element, textRange(element), parents(element))
}

private class YamlContentGenerator: ContentGenerator {
    override fun generateContent(compositeKey: List<Literal>, value: String): String =
        compositeKey.foldRightIndexed(value, { i, key, acc ->
            val caret = if (i == 0) "" else "\n"
            val tab = tabChar.repeat(i)
            "$caret$tab${key.text}: $acc"
        })

    override fun getType(): LocalizationType = LocalizationType(listOf(YAMLFileType.YML), "general")
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
        val (element, anchor) = if (item.project.commonSettings().extractSorted) {
            val before = props.takeWhile {it.name ?: "" < key}
            if (before.isEmpty()) {
                Pair(separator, obj.addBefore(keyValue, props.first()))
            } else {
                Pair(keyValue, obj.addAfter(separator, before.last()))
            }
        } else {
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

    override fun isPreferred(project: Project): Boolean =
        project.yamlSettings().preferYamlFilesGeneration
}