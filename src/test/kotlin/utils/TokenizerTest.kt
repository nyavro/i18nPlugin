package utils

import com.eny.i18n.plugin.key.lexer.KeySeparator
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.key.lexer.NsSeparator
import com.eny.i18n.plugin.key.lexer.Tokenizer
import com.eny.i18n.plugin.utils.KeyElement
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
            listOf(KeySeparator, Literal("some", 4), KeySeparator),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeUnresolvedTemplate() {
        val keyElement = KeyElement.template("\${reh}")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
            listOf(
                Literal("*", 6, true)
            ),
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
            listOf(KeySeparator, Literal("some", 4), KeySeparator),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeUnresolvedTemplateCustomSeparator() {
        val keyElement = KeyElement.template("\${rel}")
        val tokenizer = Tokenizer("^", "#")
        assertEquals(
            listOf(
                Literal("*", 6, true)
            ),
            tokenizer.tokenize(keyElement)
        )
    }
}