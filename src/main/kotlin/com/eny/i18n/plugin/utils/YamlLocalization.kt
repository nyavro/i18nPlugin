package com.eny.i18n.plugin.utils

import com.eny.i18n.Localization
import com.eny.i18n.FileType
import org.jetbrains.yaml.YAMLFileType

class YamlLocalization : Localization {
    override fun types(): List<FileType> = listOf(FileType(YAMLFileType.YML, listOf("yaml")))
}