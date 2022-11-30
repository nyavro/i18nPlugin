package com.eny.i18n.plugin.ide.references.translation

import com.eny.i18n.plugin.utils.YamlReferenceAssistant
import org.jetbrains.yaml.psi.YAMLKeyValue

/**
 * Provides navigation from i18n key to it's value in json
 */
class YamlReferenceContributor: TranslationToCodeReferenceContributor<YAMLKeyValue>(YamlReferenceAssistant())