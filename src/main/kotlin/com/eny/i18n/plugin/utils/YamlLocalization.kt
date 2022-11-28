package com.eny.i18n.plugin.utils

import com.eny.i18n.Localization
import com.eny.i18n.LocalizationType
import org.jetbrains.yaml.YAMLFileType

class YamlLocalization : Localization {
    override fun types(): List<LocalizationType> = listOf(LocalizationType(YAMLFileType.YML, listOf("yaml")))
}