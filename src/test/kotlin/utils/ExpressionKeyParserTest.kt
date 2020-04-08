package utils

import com.eny.i18n.plugin.utils.KeyElement
import org.junit.Test
import kotlin.test.assertEquals

internal class ExpressionKeyParserTest : TestBase {

//fileName:ROOT.Key2.Key3                   /                       / fileName{8}:ROOT{4}.Key2{4}.Key3{4}
    @Test
    fun parseSimpleLiteral() {
        val elements = listOf(
            KeyElement.literal("fileName:ROOT.Key2.Key3")
        )
        val parsed = parse(elements)
        assertEquals("fileName{8}:ROOT{4}.Key2{4}.Key3{4}", toTestString(parsed))
        assertEquals("fileName:ROOT.Key2.Key3", parsed?.source)
    }

//fileName:ROOT.Key2.Key3.                  /                       / fileName{8}:ROOT{4}.Key2{4}.Key3{4}.{0}
    @Test
    fun parseSimpleLiteral2() {
        val elements = listOf(
            KeyElement.literal("fileName:ROOT.Key2.Key3.")
        )
        val parsed = parse(elements)
        assertEquals("fileName{8}:ROOT{4}.Key2{4}.Key3{4}.{0}", toTestString(parsed))
        assertEquals("fileName:ROOT.Key2.Key3.", parsed?.source)
    }
}
