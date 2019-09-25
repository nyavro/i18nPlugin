package tree

import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.Tree
import com.eny.i18n.plugin.utils.Literal
import org.junit.Assert.*
import org.junit.Test

class TestTree(val value: String, val children: List<TestTree> = listOf()) : Tree<String> {

    override fun findChild(name: String) = children.find {item -> item.value==name}
    override fun isTree() = children.isNotEmpty()
    override fun value() = value
}

fun root(tree: TestTree) = TestTree("", listOf(tree))

class CompositeKeyResolverTest {

    @Test
    fun resolveElementByKey() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKey(
            listOf(Literal("root1"), Literal("key1"), Literal("subkey1")),
                root(TestTree("root1",
                    listOf(
                        TestTree("key1", listOf(TestTree("subkey1"))),
                        TestTree("key2")
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isLeaf() ?: false)
        assertEquals("subkey1", property.element?.value())
        assertTrue(property.unresolved.isEmpty())
        assertEquals(property.path, listOf(Literal("root1"), Literal("key1"), Literal("subkey1")))
    }

    @Test
    fun resolveObjectByKey() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKey(
            listOf(Literal("root2"), Literal("key3")),
            root(
                TestTree("root2",
                    listOf(
                        TestTree("key3", listOf(TestTree("subkey2"))),
                        TestTree("key4")
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
        assertEquals("key3", property.element?.value())
        assertTrue(property.unresolved.isEmpty())
        assertEquals(property.path, listOf(Literal("root2"), Literal("key3")))
    }

    @Test
    fun partialResolve() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKey(
            listOf(Literal("root3"), Literal("key7"), Literal("subkey5")),
            root(
                TestTree("root3",
                    listOf(
                        TestTree("key5", listOf(TestTree("subkey3"))),
                        TestTree("key6"),
                        TestTree("key7", listOf(TestTree("subkey4")))
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
        assertEquals("key7", property.element?.value())
        assertEquals(listOf(Literal("subkey5")), property.unresolved)
        assertEquals(property.path, listOf(Literal("root3"), Literal("key7")))
    }

    @Test
    fun partialResolve2() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKey(
            listOf(Literal("root4"), Literal("key11"), Literal("subkey7")),
            root(
                TestTree("root4",
                    listOf(
                        TestTree("key8", listOf(TestTree("subkey5"))),
                        TestTree("key9"),
                        TestTree("key10", listOf(TestTree("subkey6")))
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
        assertEquals("root4", property.element?.value())
        assertEquals(listOf(Literal("key11"), Literal("subkey7")), property.unresolved)
        assertEquals(property.path, listOf(Literal("root4")))
    }

    @Test
    fun wholeKeyUnresolved() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKey(
            listOf(Literal("root6"), Literal("key12"), Literal("subkey8")),
            root(
                TestTree("root5",
                    listOf(
                        TestTree("key12"),
                        TestTree("key13"),
                        TestTree("key14")
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
//        assertEquals("root4", property.element?.value())
        assertEquals(listOf(Literal("root6"), Literal("key12"), Literal("subkey8")), property.unresolved)
        assertTrue(property.path.isEmpty())
    }

    @Test
    fun stopResolvingAfterFirstFailure() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKey(
            listOf(Literal("root8"), Literal("key15"), Literal("subkey9")),
            root(
                TestTree("root7",
                    listOf(
                        TestTree("key15", listOf(TestTree("subkey9"))),
                        TestTree("key16"),
                        TestTree("key17", listOf(TestTree("subkey6")))
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
        assertEquals("", property.element?.value())
        assertEquals(listOf(Literal("root8"), Literal("key15"), Literal("subkey9")), property.unresolved)
        assertTrue(property.path.isEmpty())
    }

    @Test
    fun resolvesToEmptyElementOnEmptyTree() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKey(
            listOf(Literal("root9"), Literal("key18"), Literal("subkey10")),
            null
        )
        assertNull(property.element)
        assertEquals(listOf(Literal("root9"), Literal("key18"), Literal("subkey10")), property.unresolved)
        assertTrue(property.path.isEmpty())
    }

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
        assertEquals(property.path, listOf(Literal("base3"), Literal("sub5")))
    }
}