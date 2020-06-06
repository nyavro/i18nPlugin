package com.eny.i18n.plugin.utils.generator.code

import com.eny.i18n.plugin.utils.flip

interface CodeGenerator {
    fun extension(): String
    fun generate(key: String, index: Int = 0): String
    fun multiGenerate(vararg keys: String): String = keys.mapIndexed(::generate.flip()).joinToString()
}