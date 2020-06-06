package com.eny.i18n.plugin.utils.generator.translation

interface TranslationGenerator {
    fun extension(): String
    fun generateContent(root: String, first: String, key: String, value: String): String
    fun generateContent(root: String, first: String, second: String, key: String, value: String): String
}