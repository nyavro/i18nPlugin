package com.eny.i18n.plugin.utils

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.util.TextRange

interface AnnotationFacade {
    fun compositeKeyFullBounds(fullKey: FullKey): TextRange
    fun unresolved(fullKey: FullKey, resolvedPath: List<Literal>): TextRange
}

class AnnotationHolderFacade(val annotationHolder: AnnotationHolder?, val textRange: TextRange) : AnnotationFacade {

    override fun unresolved(fullKey: FullKey, resolvedPath: List<Literal>): TextRange {
        val compositeKeyStartOffset = fullKey.compositeKeyStartOffset()
        val unresolvedStartOffset = compositeKeyStartOffset + 1 + tokensLength(resolvedPath)
        return TextRange(
            unresolvedStartOffset,
            textRange.endOffset - 1
        )
    }

    override fun compositeKeyFullBounds(fullKey: FullKey) =
        TextRange(
            fullKey.compositeKeyStartOffset(),
            textRange.endOffset - 1
        )

    private fun FullKey.compositeKeyStartOffset(): Int =
        textRange.startOffset + (this.ns?.let { text -> text.length + 1 } ?: 0) + 1

}