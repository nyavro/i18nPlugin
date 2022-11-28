package com.eny.i18n.plugin.localization.yaml

import com.eny.i18n.ContentGenerator
import com.eny.i18n.plugin.factory.LocalizationFactory
import com.eny.i18n.plugin.factory.TranslationReferenceAssistant
import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReferenceProvider
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.utils.CollectingSequence
import com.eny.i18n.plugin.utils.PluginBundle
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
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


class YamlLocalizationFactory: LocalizationFactory {
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

