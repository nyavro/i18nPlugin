package com.eny.i18n.extensions.localization.yaml

import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReferenceContributor
import org.jetbrains.yaml.psi.YAMLKeyValue

/**
 * Provides navigation from i18n key to it's value in json
 */
class YamlReferenceContributor: TranslationToCodeReferenceContributor<YAMLKeyValue>(YamlReferenceAssistant())