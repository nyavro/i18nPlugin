package utils

import com.eny.i18n.plugin.key.lexer.*
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.nullableToList
import org.junit.Test
import kotlin.test.assertEquals

internal class TokenizerTest {

    @Test
    fun testTokenizeLiteral() {
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
    fun testTokenizeLiteral2() {
        val keyElement = KeyElement.literal(":::")
        val tokenizer = NsKeyTokenizer(":", ".")
        assertEquals(
            listOf(NsSeparator, NsSeparator, NsSeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun testTokenizeLiteral3() {
        val keyElement = KeyElement.literal(":.:.:")
        val tokenizer = NsKeyTokenizer(":", ".")
        assertEquals(
            listOf(NsSeparator, KeySeparator, NsSeparator, KeySeparator, NsSeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun testTokenizeLiteral4() {
        val keyElement = KeyElement.literal("...")
        val tokenizer = NsKeyTokenizer(":", ".")
        assertEquals(
            listOf(KeySeparator, KeySeparator, KeySeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun testTokenizeLiteral5() {
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
    fun testTokenizeLiteralCustomSeparator() {
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
    fun testTokenizeLiteralCustomSeparator2() {
        val keyElement = KeyElement.literal("###")
        val tokenizer = NsKeyTokenizer("#", "@")
        assertEquals(
            listOf(NsSeparator, NsSeparator, NsSeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun testTokenizeLiteralCustomSeparator3() {
        val keyElement = KeyElement.literal("#$#$#")
        val tokenizer = NsKeyTokenizer("#", "$")
        assertEquals(
            listOf(NsSeparator, KeySeparator, NsSeparator, KeySeparator, NsSeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun testTokenizeLiteralCustomSeparator4() {
        val keyElement = KeyElement.literal("$$$")
        val tokenizer = NsKeyTokenizer("%", "$")
        assertEquals(
            listOf(KeySeparator, KeySeparator, KeySeparator),
            tokenizer.tokenize(keyElement.nullableToList()).second
        )
    }

    @Test
    fun testTokenizeLiteralCustomSeparator5() {
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