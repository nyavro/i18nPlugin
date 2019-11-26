package com.eny.i18n.plugin.utils

import com.intellij.openapi.util.TextRange
import kotlin.math.max

interface AnnotationFacade {
    fun compositeKeyFullBounds(fullKey: FullKey): TextRange
    fun unresolvedKey(fullKey: FullKey, resolvedPath: List<Literal>): TextRange
    fun unresolvedNs(fullKey: FullKey): TextRange
}

class AnnotationHolderFacade(private val textRange: TextRange) : AnnotationFacade {

    override fun unresolvedNs(fullKey: FullKey): TextRange =
        safeRange(
            textRange.startOffset + 1,
            textRange.startOffset + 1 + (fullKey.ns?.length ?: 0)
        )

    override fun unresolvedKey(fullKey: FullKey, resolvedPath: List<Literal>): TextRange =
        safeRange(
            fullKey.compositeKeyStartOffset() + tokensLength(resolvedPath) + (if (resolvedPath.isNotEmpty()) 1 else 0),
            fullKey.compositeKeyEndOffset()
        )

    override fun compositeKeyFullBounds(fullKey: FullKey) =
        safeRange(
            fullKey.compositeKeyStartOffset(),
            fullKey.compositeKeyEndOffset()
        )

    private fun FullKey.compositeKeyStartOffset(): Int =
        textRange.startOffset + (this.ns?.let { text -> text.length + 1 } ?: 0) + 1

    private fun FullKey.compositeKeyEndOffset(): Int =
        textRange.startOffset + 1 + this.source.length

    private fun safeRange(start: Int, end: Int): TextRange = TextRange(start, max(end, start))
}