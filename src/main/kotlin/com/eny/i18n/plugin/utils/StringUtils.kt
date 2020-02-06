package com.eny.i18n.plugin.utils

fun String.isQuoted(): Boolean =
    (this.length > 1) && listOf("\"", "'", "`").any {quote -> this.startsWith(quote) && this.endsWith(quote)}

fun String.unQuote(): String {
    return listOf('\'', '\"', '`').fold(this) {
        acc, quote ->
            if (acc.endsWith(quote) && acc.startsWith(quote))
                acc.substring(1, this.length - 1)
            else acc
    }
}