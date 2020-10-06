package com.eny.i18n.plugin.utils.generator.code

import com.eny.i18n.plugin.utils.flip
import kotlin.random.Random

interface CodeGenerator {
    fun ext(): String
    fun generate(key: String, index: Int = 0): String
    fun multiGenerate(vararg keys: String): String = keys.mapIndexed(::generate.flip()).joinToString()
    fun generateInvalid(key: String): String
    fun generateBlock(text: String, index: Int = 0): String
}