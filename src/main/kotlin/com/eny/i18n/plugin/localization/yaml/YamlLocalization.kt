package com.eny.i18n.plugin.localization.yaml

import com.eny.i18n.plugin.factory.LocalizationFactory
import com.eny.i18n.plugin.factory.TranslationReferenceAssistant
import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReferenceProvider
import com.eny.i18n.plugin.utils.CollectingSequence
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parents
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue


class YamlLocalizationFactory: LocalizationFactory {
    override fun referenceAssistant(): TranslationReferenceAssistant<YAMLKeyValue> = YamlReferenceAssistant()
}

private class YamlReferenceAssistant : TranslationReferenceAssistant<YAMLKeyValue> {

    private val provider = TranslationToCodeReferenceProvider()

    override fun pattern(): ElementPattern<out YAMLKeyValue> = PlatformPatterns.psiElement(YAMLKeyValue::class.java)

    private fun parents(element: YAMLKeyValue): List<String> =
        CollectingSequence(element.parents(true)) {
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

