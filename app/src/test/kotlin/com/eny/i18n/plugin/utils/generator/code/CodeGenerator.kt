package com.eny.i18n.plugin.utils.generator.code

interface CodeGenerator {
    fun ext(): String
    fun generate(key: String, index: Int = 0): String
    fun multiGenerate(vararg keys: String): String = keys.mapIndexed {index, key -> generate(key, index)}.joinToString()
    fun generateInvalid(key: String): String
    fun generateBlock(text: String, index: Int = 0): String
}