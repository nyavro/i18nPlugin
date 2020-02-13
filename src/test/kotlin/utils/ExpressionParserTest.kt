package utils

import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.KeyElementType
import com.eny.i18n.plugin.utils.KeysNormalizer
import org.junit.Test
import kotlin.test.assertEquals

internal class ExpressionParserTest {
    @Test
    fun parse() {
        val text = ":ROOT.Key1.Key31"
        val elements = listOf(
            KeyElement("`", "`", KeyElementType.LITERAL),
            KeyElement("$", "$", KeyElementType.LITERAL),
            KeyElement("{", "{", KeyElementType.LITERAL),
            KeyElement("fileExpr", "sample", KeyElementType.TEMPLATE),
            KeyElement("}", "}", KeyElementType.LITERAL),
            KeyElement(text, text, KeyElementType.LITERAL),
            KeyElement("`", "`", KeyElementType.LITERAL)
        )
        val parser = KeysNormalizer()
        val expected = listOf(
            KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
            KeyElement(text, text, KeyElementType.LITERAL)
        )
        assertEquals(parser.normalize(elements), expected)
    }
}