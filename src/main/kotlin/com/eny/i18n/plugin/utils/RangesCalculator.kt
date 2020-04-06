package com.eny.i18n.plugin.utils

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
        textRange.startOffset + (this.ns?.let { text -> text.length + nsSeparatorOffset } ?: 0) + quoteOffset

    private fun FullKey.compositeKeyEndOffset(): Int =
        textRange.startOffset + quoteOffset + this.source.length

    private fun safeRange(start: Int, end: Int): TextRange = TextRange(start, max(end, start))
}