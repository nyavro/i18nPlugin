package com.eny.i18n.plugin.localization.json

import com.eny.i18n.plugin.factory.ContentGenerator
import com.eny.i18n.plugin.factory.LocalizationFactory
import com.eny.i18n.plugin.factory.LocalizationType
import com.eny.i18n.plugin.factory.TranslationReferenceAssistant
import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReferenceProvider
import com.eny.i18n.plugin.ide.settings.commonSettings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.tree.Tree
import com.eny.i18n.plugin.utils.CollectingSequence
import com.eny.i18n.plugin.utils.PluginBundle
import com.eny.i18n.plugin.utils.unQuote
import com.fasterxml.jackson.core.io.JsonStringEncoder
import com.intellij.json.JsonElementTypes
import com.intellij.json.JsonFileType
import com.intellij.json.JsonLanguage
import com.intellij.json.json5.Json5FileType
import com.intellij.json.psi.*
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parents

private val tabChar = "  "

class JsonLocalizationFactory: LocalizationFactory {

    override fun elementTreeFactory(): (file: PsiElement) -> PsiElementTree? = {
        file ->
            if (file is JsonFile) JsonElementTree.create(file)
            else if (file is JsonObject) JsonElementTree(file)
            else null
    }

    override fun contentGenerator(): ContentGenerator = JsonContentGenerator()
    override fun referenceAssistant(): TranslationReferenceAssistant<JsonStringLiteral> = JsonReferenceAssistant()
}

/**
 * Tree wrapper around json psi tree
 */
private class JsonElementTree(val element: PsiElement): PsiElementTree() {
    override fun value(): PsiElement = element
    override fun isTree(): Boolean = element is JsonObject
    override fun findChild(name: String): Tree<PsiElement>? =
            (element as JsonObject).findProperty(name)?.value?.let{ JsonElementTree(it) }
    override fun findChildren(prefix: String): List<Tree<PsiElement>> =
            element
                    .node
                    .getChildren(TokenSet.create(JsonElementTypes.PROPERTY))
                    .asList()
                    .map {item -> item.firstChildNode.psi}
                    .filter {it.text.unQuote().startsWith(prefix)}
                    .map {JsonElementTree(it)}

    companion object {
        /**
         * Creates instance of JsonElementTree
         */
        fun create(file: PsiElement): JsonElementTree? =
                PsiTreeUtil
                        .getChildOfType(file, JsonObject::class.java)
                        ?.let{ JsonElementTree(it)}
    }
}

private class JsonReferenceAssistant : TranslationReferenceAssistant<JsonStringLiteral> {

    private val provider = TranslationToCodeReferenceProvider()

    override fun pattern(): ElementPattern<out JsonStringLiteral> = PlatformPatterns.psiElement(JsonStringLiteral::class.java)

    override fun references(element: JsonStringLiteral): List<PsiReference> =
        if (element.isPropertyName && element.textLength > 1) {
            provider.getReferences(element, textRange(element), parents(element))
        } else {
            emptyList()
        }

    private fun parents(element: JsonStringLiteral): List<String> =
        CollectingSequence(element.parents()) {
            when {
                it is JsonProperty -> it.name
                it is JsonFile -> it.name.substringBeforeLast(".")
                else -> null
            }
        }.toList().reversed()

    private fun textRange(element: JsonStringLiteral): TextRange = TextRange(1, element.textLength - 1)
}

/**
 * Generates JSON translation content
 */
private class JsonContentGenerator: ContentGenerator {

    override fun isPreferred(project: Project): Boolean = false

    override fun generateContent(compositeKey: List<Literal>, value: String): String {
        val escapedValue = String(JsonStringEncoder.getInstance().quoteAsString(value))
        return compositeKey.foldRightIndexed("\"$escapedValue\"", { i, key, acc ->
            val tab = tabChar.repeat(i)
            "{\n$tabChar$tab\"${key.text}\": $acc\n$tab}"
        })
    }

    override fun getType(): LocalizationType = LocalizationType(listOf(JsonFileType.INSTANCE, Json5FileType.INSTANCE), "general")
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
        val (element, anchor) = if (item.project.commonSettings().extractSorted) {
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