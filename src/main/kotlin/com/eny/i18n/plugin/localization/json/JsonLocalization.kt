package com.eny.i18n.plugin.localization.json

import com.eny.i18n.plugin.factory.LocalizationFactory
import com.eny.i18n.plugin.factory.TranslationReferenceAssistant
import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReferenceProvider
import com.eny.i18n.plugin.utils.CollectingSequence
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parents

class JsonLocalizationFactory: LocalizationFactory {
    override fun referenceAssistant(): TranslationReferenceAssistant<JsonStringLiteral> = JsonReferenceAssistant()
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
        CollectingSequence(element.parents(true)) {
            when {
                it is JsonProperty -> it.name
                it is JsonFile -> it.name.substringBeforeLast(".")
                else -> null
            }
        }.toList().reversed()

    private fun textRange(element: JsonStringLiteral): TextRange = TextRange(1, element.textLength - 1)
}