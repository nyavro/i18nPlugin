package com.eny.i18n.plugin.language

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.psi.tree.IElementType

class I18nHighlighter : SyntaxHighlighter {
    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return listOf<TextAttributesKey>().toTypedArray()
    }

    override fun getHighlightingLexer(): Lexer {return I18nLexerAdapter()}
}