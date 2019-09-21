package utils

import com.eny.i18n.plugin.utils.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

//@Ignore
class InvalidExpressionTest : TestBase {

//invalid:file:literal.ROOT.Key1.Key31
    @Test
    fun parseInvalidFilenameInLiteral() {
        val invalidExpression = listOf(
            KeyElement.literal("invalid:file:literal.ROOT.Key1.Key31")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

//invalid:literal..key.ROOT.Key1.Key31
    @Test
    fun parseInvalidKeySeparatorInLiteral() {
        val invalidExpression = listOf(
                KeyElement.literal("invalid:literal..key.ROOT.Key1.Key31")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

//invalid.literal..key.ROOT.Key1.Key31
    @Test
    fun parseInvalidDefaultNsKeySeparatorInLiteral() {
        val invalidExpression = listOf(
                KeyElement.literal("invalid.literal..key.ROOT.Key1.Key31")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

//invalid.literal.test:.key.ROOT.Key1.Key31
    @Test
    fun parseInvalidDefaultNsKeySeparatorInLiteral2() {
        val invalidExpression = listOf(
                KeyElement.literal("invalid.literal.test:.key.ROOT.Key1.Key31")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

//.invalid.start.test
    @Test
    fun parseInvalidStart() {
        val invalidExpression = listOf(
                KeyElement.literal(".invalid.start.test")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

//:invalid.start.test
    @Test
    fun parseInvalidStart2() {
        val invalidExpression = listOf(
                KeyElement.literal(":invalid.start.test")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

//probably_not_a_reference_to_i18n
    @Test
    fun tooShortToBeKey() {
        val literal = listOf(
            KeyElement.literal("probably_not_a_reference_to_i18n")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(literal))
    }

//${fileExpr}:ROOT.Key1.Key31               / sample:file
    @Test
    fun parseInvalidExpression() {
        val invalidExpression = listOf(
            KeyElement.resolvedTemplate("\${fileExpr}", "sample:file"),
            KeyElement.literal(":ROOT.Key1.Key31")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

//filename:root${key}.Postfix               / .Key0.
    @Test
    fun partOfKeyIsExpression5() {
        val elements = listOf(
                KeyElement.literal("filename:root"),
                KeyElement.resolvedTemplate("\${key}", ".Key0."),
                KeyElement.literal(".Postfix")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(elements))
    }
}