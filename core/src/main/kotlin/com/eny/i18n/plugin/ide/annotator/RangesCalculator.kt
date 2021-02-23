package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.intellij.openapi.util.TextRange
import kotlin.math.max

/**
 * Annotation utils
 */
interface RangesCalculator {
    /**
     * Calculates bounds of composite key
     */
    fun compositeKeyFullBounds(fullKey: FullKey): TextRange

    /**
     * Calculates bounds of unresolved part of key
     */
    fun unresolvedKey(fullKey: FullKey, resolvedPath: List<Literal>): TextRange

    /**
     * Calculates bounds of unresolved namespace
     */
    fun unresolvedNs(fullKey: FullKey): TextRange
}

/**
 * Annotation utils implementation
 */
class KeyRangesCalculator(private val textRange: TextRange, isQuoted: Boolean = true) : RangesCalculator {

    private val quoteOffset = if (isQuoted) 1 else 0
    private val nsSeparatorOffset = 1
    private val keySeparatorOffset = 1

    override fun unresolvedNs(fullKey: FullKey): TextRange =
        safeRange(
            textRange.startOffset + quoteOffset,
            textRange.startOffset + quoteOffset + (fullKey.ns?.length ?: 0)
        )

    override fun unresolvedKey(fullKey: FullKey, resolvedPath: List<Literal>): TextRange =
        safeRange(
            fullKey.compositeKeyStartOffset() + tokensLength(resolvedPath) + (if (resolvedPath.isNotEmpty()) keySeparatorOffset else 0),
            fullKey.compositeKeyEndOffset()
        )

    override fun compositeKeyFullBounds(fullKey: FullKey) =
        safeRange(
            fullKey.compositeKeyStartOffset(),
            fullKey.compositeKeyEndOffset()
        )

    private fun FullKey.compositeKeyStartOffset(): Int =
        textRange.startOffset + (this.ns?.let { it.length + nsSeparatorOffset } ?: 0) + quoteOffset

    private fun FullKey.compositeKeyEndOffset(): Int =
        textRange.startOffset + quoteOffset + this.source.length

    private fun safeRange(start: Int, end: Int): TextRange = TextRange(start, max(end, start))

    private fun tokensLength(tokens: List<Literal>):Int {
        val lengths = tokens.map { n -> n.length}.filter { v -> v > 0}
        val dotCorrection = 0//tokens.map { n -> n.dot}.sum()
        return lengths.sum() + (if (lengths.isEmpty()) 0 else (lengths.size - 1)) - dotCorrection
    }
}