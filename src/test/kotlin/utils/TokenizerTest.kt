package utils

import com.eny.i18n.plugin.key.lexer.KeySeparator
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.key.lexer.NsKeyTokenizer
import com.eny.i18n.plugin.key.lexer.NsSeparator
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.nullableToList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TokenizerTest {

    @Test
    fun tokenizeLiteral() {
        val keyElement = KeyElement.literal("item.value:some.test:another")
        val tokenizer = NsKeyTokenizer(":", ".")
        val keySeparator = KeySeparator
        val nsSeparator = NsSeparator
        assertEquals(
            listOf(
                Literal("item", 4),
                keySeparator,
                Literal("value", 5),
                nsSeparator,
                Literal("some", 4),
                keySeparator,
                Literal("test", 4),
                nsSeparator,
                Literal("another", 7)
            ),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun tokenizeLiteral2() {
        val keyElement = KeyElement.literal(":::")
        val tokenizer = NsKeyTokenizer(":", ".")
        assertEquals(
            listOf(NsSeparator, NsSeparator, NsSeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun tokenizeLiteral3() {
        val keyElement = KeyElement.literal(":.:.:")
        val tokenizer = NsKeyTokenizer(":", ".")
        assertEquals(
            listOf(NsSeparator, KeySeparator, NsSeparator, KeySeparator, NsSeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun tokenizeLiteral4() {
        val keyElement = KeyElement.literal("...")
        val tokenizer = NsKeyTokenizer(":", ".")
        assertEquals(
            listOf(KeySeparator, KeySeparator, KeySeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun tokenizeLiteral5() {
        val keyElement = KeyElement.literal(".some.")
        val tokenizer = NsKeyTokenizer(":", ".")
        assertEquals(
            listOf(KeySeparator, Literal("some", 4), KeySeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

//    @Test
//    fun tokenizeUnresolvedTemplate() {
//        val keyElement = KeyElement.template("\${reh}")
//        val tokenizer = NsKeyTokenizer(":", ".")
//        assertEquals(
//            listOf(
//                Literal("*", 6, true)
//            ),
//            tokenizer.tokenize(keyElement.nullableToList()).second
//        )
//    }

    @Test
    fun tokenizeLiteralCustomSeparator() {
        val keyElement = KeyElement.literal("item#value:some#test:another")
        val tokenizer = NsKeyTokenizer(":", "#")
        val keySeparator = KeySeparator
        val nsSeparator = NsSeparator
        assertEquals(
            listOf(
                Literal("item", 4),
                keySeparator,
                Literal("value", 5),
                nsSeparator,
                Literal("some", 4),
                keySeparator,
                Literal("test", 4),
                nsSeparator,
                Literal("another", 7)
            ),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun tokenizeLiteralCustomSeparator2() {
        val keyElement = KeyElement.literal("###")
        val tokenizer = NsKeyTokenizer("#", "@")
        assertEquals(
            listOf(NsSeparator, NsSeparator, NsSeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun tokenizeLiteralCustomSeparator3() {
        val keyElement = KeyElement.literal("#$#$#")
        val tokenizer = NsKeyTokenizer("#", "$")
        assertEquals(
            listOf(NsSeparator, KeySeparator, NsSeparator, KeySeparator, NsSeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun tokenizeLiteralCustomSeparator4() {
        val keyElement = KeyElement.literal("$$$")
        val tokenizer = NsKeyTokenizer("%", "$")
        assertEquals(
            listOf(KeySeparator, KeySeparator, KeySeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun tokenizeLiteralCustomSeparator5() {
        val keyElement = KeyElement.literal("^some^")
        val tokenizer = NsKeyTokenizer("*", "^")
        assertEquals(
            listOf(KeySeparator, Literal("some", 4), KeySeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

//    @Test
//    fun tokenizeUnresolvedTemplateCustomSeparator() {
//        val keyElement = KeyElement.template("\${rel}")
//        val tokenizer = NsKeyTokenizer("^", "#")
//        assertEquals(
//            listOf(
//                Literal("*", 6, true)
//            ),
//            tokenizer.tokenize(keyElement.nullableToList()).second
//        )
//    }
}