package utils

import com.eny.i18n.plugin.utils.*
import org.junit.Test
import kotlin.test.assertEquals

class TokenizerTest {
    @Test
    fun tokenizeLiteral() {
        val keyElement = KeyElement.fromLiteral("item.value:some.test:another")
        val tokenizer = Tokenizer()
        assertEquals(
            listOf(Literal("item"), KeySeparator("."), Literal("value"), FileNameSeparator(":"), Literal("some"), KeySeparator("."), Literal("test"), FileNameSeparator(":"), Literal("another")),
            tokenizer.tokenize(keyElement)
        )
    }
}