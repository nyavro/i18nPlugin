package utils

import com.eny.i18n.plugin.utils.*
import org.junit.Test
import kotlin.test.assertEquals

class TokenizerTest {

    @Test
    fun tokenizeLiteral() {
        val keyElement = KeyElement.fromLiteral("item.value:some.test:another")
        val tokenizer = Tokenizer(":", ".")
        val keySeparator = KeySeparator(".")
        val nsSeparator = NsSeparator(":")
        assertEquals(
            listOf(Literal("item"), keySeparator, Literal("value"), nsSeparator, Literal("some"), keySeparator, Literal("test"), nsSeparator, Literal("another")),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteral2() {
        val keyElement = KeyElement.fromLiteral(":::")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
            listOf(NsSeparator(":"), NsSeparator(":"), NsSeparator(":")),
            tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteral3() {
        val keyElement = KeyElement.fromLiteral(":.:.:")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
                listOf(NsSeparator(":"), KeySeparator("."), NsSeparator(":"), KeySeparator("."), NsSeparator(":")),
                tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteral4() {
        val keyElement = KeyElement.fromLiteral("...")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
                listOf(KeySeparator("."), KeySeparator("."), KeySeparator(".")),
                tokenizer.tokenize(keyElement)
        )
    }

    @Test
    fun tokenizeLiteral5() {
        val keyElement = KeyElement.fromLiteral(".some.")
        val tokenizer = Tokenizer(":", ".")
        assertEquals(
                listOf(KeySeparator("."), Literal("some"), KeySeparator(".")),
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
                        Literal("abc"),
                        keySeparator,
                        Literal("def"),
                        nsSeparator,
                        Literal("ghi")
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
            listOf(Asterisk),
            tokenizer.tokenize(keyElement)
        )
    }
}