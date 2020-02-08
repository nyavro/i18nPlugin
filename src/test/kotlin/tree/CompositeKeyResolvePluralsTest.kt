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
        val properties = resolver.tryToResolvePlural(
            resolver.resolveCompositeKey(
                listOf(Literal("base1"), Literal("sub1"), Literal("plural1")),
                root(
                    TestTree("base1",
                        listOf(
                            TestTree(
                                "sub1",
                                listOf(
                                    TestTree("plural1-1"),
                                    TestTree("plural1-2"),
                                    TestTree("plural1-5")
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
                assertEquals("plural1-$index", property.element?.value())
                assertTrue(property.unresolved.isEmpty())
                assertEquals(property.path, listOf(Literal("base1"), Literal("sub1"), Literal("plural1")))
        }
    }

    @Test
    fun resolveCustomPluralSeparator() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val properties = resolver.tryToResolvePlural(
            resolver.resolveCompositeKey(
                listOf(Literal("base1"), Literal("sub1"), Literal("plural1")),
                root(
                    TestTree("base1",
                        listOf(
                            TestTree(
                                "sub1",
                                listOf(
                                    TestTree("plural1%1"),
                                    TestTree("plural1%2"),
                                    TestTree("plural1%5")
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
            assertEquals("plural1%$index", property.element?.value())
            assertTrue(property.unresolved.isEmpty())
            assertEquals(property.path, listOf(Literal("base1"), Literal("sub1"), Literal("plural1")))
        }
    }

    @Test
    fun resolvePluralSkippedForLeaf() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val properties = resolver.tryToResolvePlural(
            resolver.resolveCompositeKey(
                listOf(Literal("base2"), Literal("sub3"), Literal("plural2-2")),
                root(
                    TestTree("base2",
                        listOf(
                            TestTree(
                                "sub3",
                                listOf(
                                    TestTree("plural2-1"),
                                    TestTree("plural2-2"),
                                    TestTree("plural2-5")
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
        assertEquals("plural2-2", property.element?.value())
        assertTrue(property.unresolved.isEmpty())
        assertEquals(property.path, listOf(Literal("base2"), Literal("sub3"), Literal("plural2-2")))
    }

    @Test
    fun resolvePluralSkippedForLongUnresolvedPaths() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val properties = resolver.tryToResolvePlural(
            resolver.resolveCompositeKey(
                listOf(Literal("base3"), Literal("sub5")),
                root(
                    TestTree("base3",
                        listOf(
                            TestTree(
                                "sub5",
                                listOf(
                                    TestTree("plural3-1"),
                                    TestTree("plural3-2"),
                                    TestTree("plural3-5")
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
        assertEquals("sub5", property.element?.value())
        assertTrue(property.unresolved.isEmpty())
        assertEquals(listOf(Literal("base3"), Literal("sub5")), property.path)
    }
}