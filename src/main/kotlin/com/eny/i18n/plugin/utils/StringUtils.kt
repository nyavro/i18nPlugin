package com.eny.i18n.plugin.utils

/**
 * Checks if string is quoted
 */
fun String.isQuoted(): Boolean =
    (this.length > 1) && listOf("\"", "'", "`").any {quote -> this.startsWith(quote) && this.endsWith(quote)}

/**
 * Unquotes a string
 */
fun String.unQuote(): String = if (this.isQuoted()) this.substring(1, this.length - 1) else this

/**
 * String ellipsis
 */
fun String.ellipsis(maxLen:Int): String =
    if (this.length > maxLen) {
        this.substring(0, maxLen) + "..."
    } else {
        this
    }