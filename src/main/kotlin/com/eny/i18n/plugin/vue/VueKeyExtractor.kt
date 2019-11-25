package com.eny.i18n.plugin.vue

import com.eny.i18n.plugin.utils.unQuote

class VueKeyExtractor {
    fun extract(text: String): String? =
        if (text.contains("\$t(")) {
            text.substringAfter("\$t(").substringBefore(")").substringBefore(",").unQuote()
        } else null
}
