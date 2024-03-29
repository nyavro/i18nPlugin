package com.eny.i18n.extensions.localization.yaml

import com.eny.i18n.ContentGenerator
import com.eny.i18n.Localization
import com.eny.i18n.LocalizationConfig
import com.eny.i18n.LocalizationFileType
import com.eny.i18n.plugin.ConfigurationProperty
import com.eny.i18n.*
import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReferenceProvider
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.tree.Tree
import com.eny.i18n.plugin.utils.PluginBundle
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.icons.AllIcons
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
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
import javax.swing.Icon

class YamlLocalization : Localization<YAMLKeyValue> {
    override fun types(): List<LocalizationFileType> = listOf(LocalizationFileType(YAMLFileType.YML, listOf("yaml")))
    override fun contentGenerator(): ContentGenerator = YamlContentGenerator()
    override fun referenceAssistant(): TranslationReferenceAssistant<YAMLKeyValue> = YamlReferenceAssistant()
    override fun elementsTree(file: PsiElement): Tree<PsiElement>? = YamlElementTree.create(file)
    override fun matches(localizationFileType: LocalizationFileType, file: VirtualFile?, fileNames: List<String>): Boolean =
        fileNames.any {
            fileName -> localizationFileType.extensions().any { ext -> "$fileName.$ext"==file?.name}
        }
    override fun icon(): Icon = AllIcons.FileTypes.Yaml
    override fun config(): LocalizationConfig {
        return object: LocalizationConfig {
            override fun id(): String = "yaml"
            override fun props(): List<ConfigurationProperty> = listOf()
        }
    }
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

class YamlReferenceAssistant : TranslationReferenceAssistant<YAMLKeyValue> {

    private val provider = TranslationToCodeReferenceProvider()

    override fun pattern(): ElementPattern<out YAMLKeyValue> = PlatformPatterns.psiElement(YAMLKeyValue::class.java)

    private fun parents(element: YAMLKeyValue): List<String> =
        element.parents(true).mapNotNull {
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