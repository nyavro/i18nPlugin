package com.eny.i18n.plugin.utils

/**
 * Returns tokens string length
 */
fun tokensLength(tokens: List<Literal>):Int {
    val lengths = tokens.map { n -> n.length}.filter { v -> v > 0}
    val dotCorrection = tokens.map { n -> n.dot}.sum()
    return lengths.sum() + (if (lengths.isEmpty()) 0 else (lengths.size - 1)) - dotCorrection
}