package com.eny.i18n.plugin.addons.yaml

import com.eny.i18n.plugin.addons.yaml.YamlLocalizationFactory
import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReferenceContributor
import org.jetbrains.yaml.psi.YAMLKeyValue

/**
 * Provides navigation from i18n key to it's value in json
 */
class YamlReferenceContributor: TranslationToCodeReferenceContributor<YAMLKeyValue>(YamlLocalizationFactory().referenceAssistant())