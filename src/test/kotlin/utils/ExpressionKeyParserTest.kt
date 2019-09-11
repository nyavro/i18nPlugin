package utils

import com.eny.i18n.plugin.utils.ExpressionKeyParser
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.KeyElementType
import org.junit.Test
import kotlin.test.assertEquals

class ExpressionKeyParserTest {
    @Test
    fun parse() {
        val elements = listOf(
            KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
            KeyElement(":ROOT.Key1.Key31", ":ROOT.Key1.Key31", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expected = FullKey(
            listOf(KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE)),
            listOf(
                KeyElement("ROOT", "ROOT", KeyElementType.LITERAL),
                KeyElement("Key1", "Key1", KeyElementType.LITERAL),
                KeyElement("Key31", "Key31", KeyElementType.LITERAL)
            )
        )
        assertEquals(parser.parse(elements), expected)
    }
}