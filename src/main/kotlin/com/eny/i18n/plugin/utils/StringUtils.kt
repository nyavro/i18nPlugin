package com.eny.i18n.plugin.utils

fun String.unQuote(): String {
    return listOf('\'', '\"', '`').fold(this) {
        acc, quote ->
            if (acc.endsWith(quote) && acc.startsWith(quote))
                acc.substring(1, this.length - 1)
            else acc
    }
}