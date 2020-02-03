package tree

import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.Tree
import com.eny.i18n.plugin.utils.Literal
import org.junit.Assert.*
import org.junit.Test

/**
 * Test tree wrapper
 */
class TestTree(private val value: String, private val children: List<TestTree> = listOf()) : Tree<String> {
    override fun findChild(name: String) = children.find {item -> item.value==name}
    override fun isTree() = children.isNotEmpty()
    override fun value() = value
    override fun findChildren(regex: Regex): List<Tree<String>> = children.filter {item -> item.value.matches(regex)}
}

fun root(tree: TestTree) = TestTree("", listOf(tree))

/**
 * CompositeKeyResolver tests
 */
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

    @Test
    fun listCompositeKeyVariants() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val variants = resolver.listCompositeKeyVariants(
            listOf(Literal("top1"), Literal("under2")),
            root(
                TestTree("top1",
                    listOf(
                        TestTree("under1", listOf(TestTree("subunder1"))),
                        TestTree("under2", listOf(TestTree("subunder2"), TestTree("busunder2"), TestTree("subunder4"))),
                        TestTree("under3", listOf(TestTree("subunder3")))
                    )
                )
            ),
            Regex("sub.*")
        )
        assertTrue(variants.isNotEmpty())
        assertEquals(2, variants.size)
        assertEquals("subunder2", variants.get(0).value())
        assertEquals("subunder4", variants.get(1).value())
    }

    @Test
    fun listCompositeKeyVariants2() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val variants = resolver.listCompositeKeyVariants(
            listOf(Literal("top2"), Literal("under5")),
            root(
                TestTree("top2",
                    listOf(
                        TestTree("under4", listOf(TestTree("subunder5"))),
                        TestTree("under5", listOf(TestTree("undsuber6"), TestTree("busunder7"), TestTree("subunder8und"), TestTree("subuder8ud"), TestTree("subu9nder8und"))),
                        TestTree("under6", listOf(TestTree("subunder9")))
                    )
                )
            ),
            Regex(".*und.*")
        )
        assertTrue(variants.isNotEmpty())
        assertEquals(4, variants.size)
        assertEquals(listOf("undsuber6", "busunder7", "subunder8und", "subu9nder8und"), variants.map {item -> item.value()})
    }

    @Test
    fun listCompositeKeyVariants3() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val variants = resolver.listCompositeKeyVariants(
            listOf(Literal("top3"), Literal("under8")),
            root(
                TestTree("top3",
                    listOf(
                        TestTree("under7", listOf(TestTree("subunder10"))),
                        TestTree("under8", listOf(TestTree("undsuber12"), TestTree("busunder13"), TestTree("subunder14und"), TestTree("subuder15ud"), TestTree("subu16nder17und"))),
                        TestTree("under9", listOf(TestTree("subunder11")))
                    )
                )
            ),
            Regex(".*und")
        )
        assertTrue(variants.isNotEmpty())
        assertEquals(2, variants.size)
        assertEquals(listOf("subunder14und", "subu16nder17und"), variants.map {item -> item.value()})
    }

    @Test
    fun listCompositeKeyVariantsOmitPlurals() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val variants = resolver.listCompositeKeyVariants(
            listOf(Literal("top4"), Literal("under10"), Literal("subunder12")),
            root(
                TestTree("top4",
                    listOf(
                        TestTree("under10",
                            listOf(
                                TestTree("subunder12",
                                    listOf(
                                        TestTree("subsub1-1"),
                                        TestTree("subsub1-2"),
                                        TestTree("subsub1-5")
                                    )
                                ),
                                TestTree("subunder13")
                            )
                        ),
                        TestTree("under11", listOf(TestTree("subunder14"))),
                        TestTree("under12", listOf(TestTree("subunder15")))
                    )
                )
            ),
            Regex("subs.*")
        )
        val list = listOf("s-tr1", "s-tr2", "str3-1", "str3-2", "str3-5", "str4-1", "str4-2", "str5", "str6-1", "str6-2", "str6-5")
        val grouped = list
            .groupBy {it.substringBeforeLast("-")}
            .entries.flatMap {
                entry -> if(entry.value.size == 3 && entry.value.containsAll(listOf(1,2,5).map{entry.key+"-"+it})) {
                    listOf(entry.key)} else entry.value
            }

        assertEquals(7, grouped.size)
        assertEquals(listOf("s-tr1", "s-tr2", "str3", "str4-1", "str4-2", "str5", "str6"), grouped)
//        assertTrue(variants.isNotEmpty())
//        assertEquals(1, variants.size)
//        assertEquals(listOf("subsub1"), variants.map {item -> item.value()})
    }
}