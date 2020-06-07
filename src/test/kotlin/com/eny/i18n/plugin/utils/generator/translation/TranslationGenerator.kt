package com.eny.i18n.plugin.utils.generator.translation

interface TranslationGenerator {
    fun ext(): String
    fun generateContent(root: String, first: String, key: String, value: String): String
    fun generateContent(root: String, first: String, second: String, key: String, value: String): String
    fun generatePlural(root: String, first: String, key: String, value1: String, value2: String, value5: String): String
}