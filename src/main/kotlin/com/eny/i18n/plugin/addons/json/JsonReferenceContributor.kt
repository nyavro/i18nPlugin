package com.eny.i18n.plugin.addons.json

import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReferenceContributor
import com.intellij.json.psi.JsonStringLiteral

/**
 * Provides navigation from i18n key to it's value in json
 */
class JsonReferenceContributor: TranslationToCodeReferenceContributor<JsonStringLiteral>(JsonLocalizationFactory().referenceAssistant())
