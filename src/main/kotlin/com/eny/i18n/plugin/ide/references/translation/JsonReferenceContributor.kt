package com.eny.i18n.plugin.ide.references.translation

import com.eny.i18n.plugin.localization.json.JsonLocalizationFactory
import com.eny.i18n.plugin.tree.KeyComposer
import com.eny.i18n.plugin.utils.CollectingSequence
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.util.parents
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue

/**
 * Provides navigation from i18n key to it's value in json
 */
class JsonReferenceContributor: TranslationToCodeReferenceContributor<JsonStringLiteral>(JsonLocalizationFactory().referenceAssistant())
