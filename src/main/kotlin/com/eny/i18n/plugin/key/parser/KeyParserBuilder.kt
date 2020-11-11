package com.eny.i18n.plugin.key.parser

import com.eny.i18n.plugin.key.lexer.NoTokenizer
import com.eny.i18n.plugin.key.lexer.NormalizingTokenizer
import com.eny.i18n.plugin.key.lexer.NsKeyTokenizer
import com.eny.i18n.plugin.key.lexer.Tokenizer
import com.eny.i18n.plugin.parser.DummyTextNormalizer
import com.eny.i18n.plugin.parser.ExpressionNormalizer
import com.eny.i18n.plugin.parser.KeyNormalizer

/**
 * Builds Key parser by given options
 */
class KeyParserBuilder private constructor (private val tokenizer: Tokenizer, private val normalizers: List<KeyNormalizer>) {

    /**
     * Builds one more step to KeyParser: adds dummy text normalizer
     */
    fun withDummyNormalizer(): KeyParserBuilder {
        return KeyParserBuilder(tokenizer, normalizers + DummyTextNormalizer())
    }

    /**
     * Builds one more step to KeyParser: adds dummy text normalizer
     */
    fun withTemplateNormalizer(): KeyParserBuilder {
        return KeyParserBuilder(tokenizer, normalizers + ExpressionNormalizer())
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