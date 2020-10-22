package com.eny.i18n.plugin.utils.generator.translation

interface TranslationGenerator {
    fun ext(): String
    fun generateContent(key: String, value: String): String
    fun generateContent(root: String, key: String, value: String): String
    fun generateContent(root: String, first: String, key: String, value: String): String
    fun generateContent(root: String, first: String, second: String, key: String, value: String): String
    fun generatePlural(root: String, first: String, key: String, value1: String, value2: String, value5: String): String
    fun generateInvalid(): String
    fun generateInvalidRoot(): String
    fun generate(root: String, vararg branches: Array<String>): String
    fun generate(vararg branches: Array<String>): String
    fun generateNamedBlock(key: String, block: String, level: Int = 0): String
    fun generateNamedBlocks(vararg blocks: Pair<String, String>): String
}
