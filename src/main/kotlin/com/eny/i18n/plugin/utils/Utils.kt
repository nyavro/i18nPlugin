package com.eny.i18n.plugin.utils

/**
 * Chain function for applying action to not empty collection
 */
inline fun <T, C: Collection<T>, R> C.whenNonEmpty(block: (C) -> R): R? {
    return if (this.isNotEmpty()) block(this) else null
}

/**
 * Chain-style predicate matcher
 */
inline fun <C> C.whenMatches(predicate: (arg: C) -> Boolean): C? {
    return if (predicate(this)) this else null
}