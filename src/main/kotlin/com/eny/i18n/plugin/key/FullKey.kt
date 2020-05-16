package com.eny.i18n.plugin.key

import com.eny.i18n.plugin.key.lexer.Literal

/**
 * Represents translation key
 */
data class FullKey(val source: String, val ns: Literal?, val compositeKey:List<Literal>, val isTemplate: Boolean = false)