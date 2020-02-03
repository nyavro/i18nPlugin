package utils

import com.eny.i18n.plugin.utils.KeysNormalizer
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.KeyElementType
import org.junit.Test
import kotlin.test.assertEquals

class ExpressionParserTest {
    @Test
    fun parse() {
        val elements = listOf(
            KeyElement("`", "`", KeyElementType.LITERAL),
            KeyElement("$", "$", KeyElementType.LITERAL),
            KeyElement("{", "{", KeyElementType.LITERAL),
            KeyElement("fileExpr", "sample", KeyElementType.TEMPLATE),
            KeyElement("}", "}", KeyElementType.LITERAL),
            KeyElement(":ROOT.Key1.Key31", ":ROOT.Key1.Key31", KeyElementType.LITERAL),
            KeyElement("`", "`", KeyElementType.LITERAL)
        )
        val parser = KeysNormalizer()
        val expected = listOf(
            KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
            KeyElement(":ROOT.Key1.Key31", ":ROOT.Key1.Key31", KeyElementType.LITERAL)
        )
        assertEquals(parser.normalize(elements), expected)
    }
}