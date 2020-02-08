package tree

import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.utils.Literal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * CompositeKeyResolver tests
 */
internal class CompositeKeyResolvePluralsTest {

    @Test
    fun resolvePluralElementByKey() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val base = "base1"
        val sub = "sub1"
        val plural = "plural1"
        val properties = resolver.tryToResolvePlural(
            resolver.resolveCompositeKey(
                listOf(Literal(base), Literal(sub), Literal(plural)),
                root(
                    TestTree(base,
                        listOf(
                            TestTree(
                                    sub,
                                listOf(
                                    TestTree("$plural-1"),
                                    TestTree("$plural-2"),
                                    TestTree("$plural-5")
                                )
                            ),
                            TestTree("sub2")
                        )
                    )
                )
            )
        )
        assertEquals(3, properties.size)
        properties.zip(listOf(1,2,5)).forEach {
            (property, index) ->
                assertTrue(property.element?.isLeaf() ?: false)
                assertEquals("$plural-$index", property.element?.value())
                assertTrue(property.unresolved.isEmpty())
                assertEquals(property.path, listOf(Literal(base), Literal(sub), Literal(plural)))
        }
    }

    @Test
    fun resolveCustomPluralSeparator() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val base = "base2"
        val sub = "sub2"
        val plural = "plural2"
        val properties = resolver.tryToResolvePlural(
            resolver.resolveCompositeKey(
                listOf(Literal(base), Literal(sub), Literal(plural)),
                root(
                    TestTree(base,
                        listOf(
                            TestTree(
                                    sub,
                                listOf(
                                    TestTree("$plural%1"),
                                    TestTree("$plural%2"),
                                    TestTree("$plural%5")
                                )
                            ),
                            TestTree("sub2")
                        )
                    )
                )
            ),
            "%"
        )
        assertEquals(3, properties.size)
        properties.zip(listOf(1,2,5)).forEach {
            (property, index) ->
            assertTrue(property.element?.isLeaf() ?: false)
            assertEquals("$plural%$index", property.element?.value())
            assertTrue(property.unresolved.isEmpty())
            assertEquals(property.path, listOf(Literal(base), Literal(sub), Literal(plural)))
        }
    }

    @Test
    fun resolvePluralSkippedForLeaf() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val base = "base3"
        val sub = "sub3"
        val plural = "plural3"
        val properties = resolver.tryToResolvePlural(
            resolver.resolveCompositeKey(
                listOf(Literal(base), Literal(sub), Literal("$plural-2")),
                root(
                    TestTree(base,
                        listOf(
                            TestTree(
                                    sub,
                                listOf(
                                    TestTree("$plural-1"),
                                    TestTree("$plural-2"),
                                    TestTree("$plural-5")
                                )
                            ),
                            TestTree("sub4")
                        )
                    )
                )
            )
        )
        assertEquals(1, properties.size)
        val property = properties.first()
        assertTrue(property.element?.isLeaf() ?: false)
        assertEquals("$plural-2", property.element?.value())
        assertTrue(property.unresolved.isEmpty())
        assertEquals(property.path, listOf(Literal(base), Literal(sub), Literal("$plural-2")))
    }

    @Test
    fun resolvePluralSkippedForLongUnresolvedPaths() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val base = "base4"
        val sub = "sub4"
        val plural = "plural4"
        val properties = resolver.tryToResolvePlural(
            resolver.resolveCompositeKey(
                listOf(Literal(base), Literal(sub)),
                root(
                    TestTree(base,
                        listOf(
                            TestTree(
                                    sub,
                                listOf(
                                    TestTree("$plural-1"),
                                    TestTree("$plural-2"),
                                    TestTree("$plural-5")
                                )
                            ),
                            TestTree("sub6")
                        )
                    )
                )
            )
        )
        assertEquals(1, properties.size)
        val property = properties.first()
        assertTrue(property.element?.isTree() ?: false)
        assertEquals(sub, property.element?.value())
        assertTrue(property.unresolved.isEmpty())
        assertEquals(listOf(Literal(base), Literal(sub)), property.path)
    }
}