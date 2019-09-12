package utils

import com.eny.i18n.plugin.utils.*
import org.junit.Test
import kotlin.test.assertNull

class InvalidExpressionTest {
    fun extractTexts(list: List<Token>) = list.map {token -> token.text()}

    @Test
    fun parseInvalidFilenameInLiteral() {
        val invalidExpression = listOf(
            KeyElement.fromLiteral("invalid:file:literal.ROOT.Key1.Key31")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

    @Test
    fun parseInvalidKeySeparatorInLiteral() {
        val invalidExpression = listOf(
                KeyElement.fromLiteral("invalid:literal..key.ROOT.Key1.Key31")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

    @Test
    fun tooShortToBeKey() {
        val literal = listOf(
            KeyElement.fromLiteral("probably_not_a_reference_to_i18n")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(literal))
    }

    @Test
    fun parseInvalidExpression() {
        val invalidExpression = listOf(
            KeyElement("\${fileExpr}", "sample:file", KeyElementType.TEMPLATE),
            KeyElement(":ROOT.Key1.Key31", ":ROOT.Key1.Key31", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }
}