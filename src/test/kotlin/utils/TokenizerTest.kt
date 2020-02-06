package utils

import com.eny.i18n.plugin.utils.*
import org.junit.Test
import kotlin.test.assertEquals

internal class TokenizerTest {

    @Test
    fun tokenizeLiteral() {
        val keyElement = KeyElement.literal("item.value:some.test:another")
        val tokenizer = Tokenizer(":", ".")
        val keySeparator = KeySeparator
        val nsSeparator = NsSeparator
        assertEquals(
            listOf(
                Literal("item", 4, 0),
                keySeparator,
                Literal("value", 5, 0),
                nsSeparator,
                Literal("some", 4, 0),
                keySeparator,
                Literal("test", 4, 0),
                nsSeparator,
                Literal("another", 7, 0)
            ),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteral2() {
        val keyElement = KeyElement.literal(":::")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
            listOf(NsSeparator, NsSeparator, NsSeparator),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteral3() {
        val keyElement = KeyElement.literal(":.:.:")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
            listOf(NsSeparator, KeySeparator, NsSeparator, KeySeparator, NsSeparator),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteral4() {
        val keyElement = KeyElement.literal("...")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
            listOf(KeySeparator, KeySeparator, KeySeparator),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteral5() {
        val keyElement = KeyElement.literal(".some.")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
            listOf(KeySeparator, Literal("some", 4, 0), KeySeparator),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeTemplate() {
        val keyElement = KeyElement("\${ref}", "abc.def:ghi", KeyElementType.TEMPLATE)
        val tokenizer = Tokenizer(":", ".")
        val keySeparator = KeySeparator
        val nsSeparator = NsSeparator
        assertEquals(
            listOf(
                Literal("abc", 6, 0),
                keySeparator,
                Literal("def", 0, 0),
                nsSeparator,
                Literal("ghi", 0, 0)
            ),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeTemplateWithDotCorrection() {
        val keyElement = KeyElement("\${ref}", ".abc.def:ghi", KeyElementType.TEMPLATE)
        val tokenizer = Tokenizer(":", ".")
        val keySeparator = KeySeparator
        val nsSeparator = NsSeparator
        assertEquals(
            listOf(
                keySeparator,
                Literal("abc", 6, 1),
                keySeparator,
                Literal("def", 0, 0),
                nsSeparator,
                Literal("ghi", 0, 0)
            ),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeUnresolvedTemplate() {
        val keyElement = KeyElement("\${ref}", null, KeyElementType.TEMPLATE)
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
            listOf(
                Literal("*", 6, 0)
            ),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeReferenceToEmptyValue() {
        val keyElement = KeyElement.resolvedTemplate("\${ref}", "")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
            listOf(),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteralCustomSeparator() {
        val keyElement = KeyElement.literal("item#value:some#test:another")
        val tokenizer = Tokenizer(":", "#")
        val keySeparator = KeySeparator
        val nsSeparator = NsSeparator
        assertEquals(
            listOf(
                Literal("item", 4, 0),
                keySeparator,
                Literal("value", 5, 0),
                nsSeparator,
                Literal("some", 4, 0),
                keySeparator,
                Literal("test", 4, 0),
                nsSeparator,
                Literal("another", 7, 0)
            ),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteralCustomSeparator2() {
        val keyElement = KeyElement.literal("###")
        val tokenizer = Tokenizer("#", "@")
        assertEquals(
            listOf(NsSeparator, NsSeparator, NsSeparator),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteralCustomSeparator3() {
        val keyElement = KeyElement.literal("#$#$#")
        val tokenizer = Tokenizer("#", "$")
        assertEquals(
            listOf(NsSeparator, KeySeparator, NsSeparator, KeySeparator, NsSeparator),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteralCustomSeparator4() {
        val keyElement = KeyElement.literal("$$$")
        val tokenizer = Tokenizer("%", "$")
        assertEquals(
            listOf(KeySeparator, KeySeparator, KeySeparator),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteralCustomSeparator5() {
        val keyElement = KeyElement.literal("^some^")
        val tokenizer = Tokenizer("*", "^")
        assertEquals(
            listOf(KeySeparator, Literal("some", 4, 0), KeySeparator),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeTemplateCustomSeparator() {
        val keyElement = KeyElement("\${ref}", "abc&def(ghi", KeyElementType.TEMPLATE)
        val tokenizer = Tokenizer("(", "&")
        val keySeparator = KeySeparator
        val nsSeparator = NsSeparator
        assertEquals(
            listOf(
                Literal("abc", 6, 0),
                keySeparator,
                Literal("def", 0, 0),
                nsSeparator,
                Literal("ghi", 0, 0)
            ),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeTemplateWithDotCorrectionCustomSeparator() {
        val keyElement = KeyElement("\${ref}", "&abc&def\$ghi", KeyElementType.TEMPLATE)
        val tokenizer = Tokenizer("$", "&")
        val keySeparator = KeySeparator
        val nsSeparator = NsSeparator
        assertEquals(
            listOf(
                keySeparator,
                Literal("abc", 6, 1),
                keySeparator,
                Literal("def", 0, 0),
                nsSeparator,
                Literal("ghi", 0, 0)
            ),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeUnresolvedTemplateCustomSeparator() {
        val keyElement = KeyElement("\${ref}", null, KeyElementType.TEMPLATE)
        val tokenizer = Tokenizer("^", "#")
        assertEquals(
            listOf(
                Literal("*", 6, 0)
            ),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeReferenceToEmptyValueCustomSeparator() {
        val keyElement = KeyElement.resolvedTemplate("\${ref}", "")
        val tokenizer = Tokenizer("@", "#")
        assertEquals(
            listOf(),
            tokenizer.tokenize(keyElement)
        )
    }
}