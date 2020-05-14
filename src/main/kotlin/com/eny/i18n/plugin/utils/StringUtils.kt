package com.eny.i18n.plugin.utils

/**
 * Checks if string is quoted
 */
fun String.isQuoted(): Boolean =
    (this.length > 1) && listOf("\"", "'", "`").any {quote -> this.startsWith(quote) && this.endsWith(quote)}

/**
 * Unquotes the string
 */
fun String.unQuote(): String {
    return listOf('\'', '\"', '`').fold(this) {
        acc, quote ->
            if (acc.endsWith(quote) && acc.startsWith(quote) && acc.length > 1)
                acc.substring(1, this.length - 1)
            else acc
    }
}

/**
 * String ellipsis
 */
fun String.ellipsis(maxLen:Int): String =
    if (this.length > maxLen) {
        this.substring(0, maxLen) + "..."
    } else {
        this
    }