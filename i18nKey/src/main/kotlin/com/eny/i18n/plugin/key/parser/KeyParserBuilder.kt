package com.eny.i18n.plugin.key.parser

import com.eny.i18n.plugin.key.KeyNormalizer
import com.eny.i18n.plugin.key.lexer.NoTokenizer
import com.eny.i18n.plugin.key.lexer.NormalizingTokenizer
import com.eny.i18n.plugin.key.lexer.NsKeyTokenizer
import com.eny.i18n.plugin.key.lexer.Tokenizer

/**
 * Builds Key parser by given options
 */
class KeyParserBuilder private constructor (private val tokenizer: Tokenizer, private val normalizers: List<KeyNormalizer>) {

    /**
     * Adds normalizer
     */
    fun withNormalizer(normalizer: KeyNormalizer): KeyParserBuilder {
        return KeyParserBuilder(tokenizer, normalizers + normalizer)
    }

    /**
     * Builds final KeyParser
     */
    fun build(): KeyParser = KeyParser(NormalizingTokenizer(tokenizer, normalizers))

    companion object {
        /**
         * Builds one step of key parser: sets tokenizer with ns and key separators
         */
        fun withSeparators(ns: String, key: String): KeyParserBuilder {
            return KeyParserBuilder(NsKeyTokenizer(ns, key), listOf())
        }

        /**
         * Builds one step of key parser: sets NoTokenizer
         */
        fun withoutTokenizer(): KeyParserBuilder {
            return KeyParserBuilder(NoTokenizer(), listOf())
        }
    }
}