package tree

import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.Tree
import com.eny.i18n.plugin.utils.Literal
import org.junit.Assert.*
import org.junit.Test

/**
 * Test tree wrapper
 */
internal class TestTree(private val value: String, private val children: List<TestTree> = listOf()) : Tree<String> {
    override fun findChild(name: String) = children.find {item -> item.value==name}
    override fun isTree() = children.isNotEmpty()
    override fun value() = value
    override fun findChildren(regex: Regex): List<Tree<String>> = children.filter {item -> item.value.matches(regex)}
}

internal fun root(tree: TestTree) = TestTree("", listOf(tree))

/**
 * CompositeKeyResolver tests
 */
internal class CompositeKeyResolverTest {

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
    fun stopResolvingAfterFirstFailure2() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKey(
            listOf(Literal("ROOT"), Literal("Key1", 6), Literal("key3", 0), Literal("key31", 5)),
            root(
                TestTree("ROOT",
                    listOf(
                        TestTree("Key1",
                            listOf(
                                TestTree("key2"),
                                TestTree("key31")
                            )
                        )
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
        assertEquals("Key1", property.element?.value())
        assertEquals(listOf(Literal("key3", 0), Literal("key31", 5)), property.unresolved)
        assertEquals(listOf(Literal("ROOT"), Literal("Key1", 6)), property.path)
    }

    @Test
    fun resolveProperty() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKeyProperty(
            listOf(Literal("main1"), Literal("submain1"), Literal("subsub2")),
            root(
                TestTree("main1",
                    listOf(
                        TestTree("submain0", listOf(TestTree("subsub0"))),
                        TestTree("submain1", listOf(TestTree("subsub1"), TestTree("subsub2"))),
                        TestTree("submain2", listOf(TestTree("subsub3")))
                    )
                )
            )
        )
        assertTrue(property?.isLeaf() ?: false)
        assertEquals("subsub2", property?.value())
    }

    @Test
    fun unresolvedPathIsNull() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKeyProperty(
            listOf(Literal("main2"), Literal("submain4"), Literal("unresolved")),
            root(
                TestTree("main2",
                    listOf(
                        TestTree("submain3", listOf(TestTree("subsub4"))),
                        TestTree("submain4", listOf(TestTree("subsub5"), TestTree("subsub6"))),
                        TestTree("submain5", listOf(TestTree("subsub7")))
                    )
                )
            )
        )
        assertNull(property)
    }

    @Test
    fun unresolvedPathIsNull2() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKeyProperty(
            listOf(Literal("main3"), Literal("submain6"), Literal("subsub8"), Literal("subsub12")),
            root(
                TestTree("main3",
                    listOf(
                        TestTree("submain6", listOf(TestTree("subsub8"))),
                        TestTree("submain7", listOf(TestTree("subsub9"), TestTree("subsub10"))),
                        TestTree("submain8", listOf(TestTree("subsub11")))
                    )
                )
            )
        )
        assertNull(property)
    }
}