package com.eny.i18n.plugin.utils

import com.eny.i18n.Localization
import com.eny.i18n.LocalizationType
import com.intellij.json.JsonFileType
import com.intellij.json.json5.Json5FileType

class JsonLocalization : Localization {
    override fun types(): List<LocalizationType> = listOf(JsonFileType.INSTANCE, Json5FileType.INSTANCE).map { LocalizationType(it) }
}