package utils

import com.eny.i18n.plugin.utils.*
import org.junit.Test
import kotlin.test.assertEquals

class TokenizerTest {

    @Test
    fun tokenizeLiteral() {
        val keyElement = KeyElement.literal("item.value:some.test:another")
        val tokenizer = Tokenizer(":", ".")
        val keySeparator = KeySeparator(".")
        val nsSeparator = NsSeparator(":")
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
            listOf(NsSeparator(":"), NsSeparator(":"), NsSeparator(":")),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteral3() {
        val keyElement = KeyElement.literal(":.:.:")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
                listOf(NsSeparator(":"), KeySeparator("."), NsSeparator(":"), KeySeparator("."), NsSeparator(":")),
                tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteral4() {
        val keyElement = KeyElement.literal("...")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
                listOf(KeySeparator("."), KeySeparator("."), KeySeparator(".")),
                tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteral5() {
        val keyElement = KeyElement.literal(".some.")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
                listOf(KeySeparator("."), Literal("some", 4, 0), KeySeparator(".")),
                tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeTemplate() {
        val keyElement = KeyElement("\${ref}", ".abc.def:ghi", KeyElementType.TEMPLATE)
        val tokenizer = Tokenizer(":", ".")
        val keySeparator = KeySeparator(".")
        val nsSeparator = NsSeparator(":")
        assertEquals(
            listOf(
                TemplateExpression("\${ref}",
                    listOf(
                        keySeparator,
                        Literal("abc", 3, 0),
                        keySeparator,
                        Literal("def", 3, 0),
                        nsSeparator,
                        Literal("ghi", 3, 0)
                    )
                )
            ),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeUnresolvedTemplate() {
        val keyElement = KeyElement("\${ref}", null, KeyElementType.TEMPLATE)
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
            listOf(TemplateExpression("\${ref}", listOf(Literal("*", 1, 0)))),
            tokenizer.tokenize(keyElement)
        )
    }
}