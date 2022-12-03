package tree

import com.eny.i18n.ContentGenerator
import com.eny.i18n.Localization
import com.eny.i18n.LocalizationFileType
import com.eny.i18n.LocalizationSource
import com.eny.i18n.plugin.factory.TranslationReferenceAssistant
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.Tree
import com.eny.i18n.plugin.utils.toBoolean
import com.intellij.json.JsonFileType
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import org.junit.Assert.*
import org.junit.jupiter.api.Test

/**
 * Test tree wrapper
 */
internal class TestTree(private val value: String, private val children: List<TestTree> = listOf()) : Tree<String> {
    override fun findChild(name: String) = children.find {item -> item.value==name}
    override fun isTree() = children.isNotEmpty()
    override fun value() = value
    override fun findChildren(prefix: String): List<Tree<String>> = children.filter { item -> item.value.startsWith(prefix)}
}

internal fun root(tree: TestTree) = TestTree("", listOf(tree))

internal fun localizationSource(tree: TestTree?): LocalizationSource {
    return LocalizationSource(
        treeAdapter(tree), "test", "parent", "test", testLocalization()
    )
}

fun testLocalization(): Localization<PsiElement> {
    return object: Localization<PsiElement> {
        override fun types(): List<LocalizationFileType> {
            TODO("Not yet implemented")
        }

        override fun contentGenerator(): ContentGenerator {
            TODO("Not yet implemented")
        }

        override fun referenceAssistant(): TranslationReferenceAssistant<PsiElement> {
            TODO("Not yet implemented")
        }

        override fun elementsTree(file: PsiElement): Tree<PsiElement>? {
            TODO("Not yet implemented")
        }

        override fun matches(localizationFileType: LocalizationFileType, file: VirtualFile?, fileNames: List<String>): Boolean {
            TODO("Not yet implemented")
        }

    }
}

fun treeAdapter(tree: Tree<String>?): Tree<PsiElement> {
    return object: Tree<PsiElement> {
        override fun findChild(name: String): Tree<PsiElement>? {
            return tree?.findChild(name)?.let{treeAdapter(it)}
        }

        override fun isTree(): Boolean {
            return tree?.isTree() ?: false
        }

        override fun value(): PsiElement {
            TODO("Not yet implemented")
        }

        override fun findChildren(prefix: String): List<Tree<PsiElement>> {
            return tree?.findChildren(prefix)?.mapNotNull {treeAdapter(it)} ?: listOf()
        }

    }
}

/**
 * CompositeKeyResolver tests
 */
internal class CompositeKeyResolverTest {

    private val type = JsonFileType.INSTANCE

    @Test
    fun resolveElementByKey() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val root = "root1"
        val key = "key1"
        val text = "subkey1"
        val property = resolver.resolveCompositeKey(
            listOf(Literal(root), Literal(key), Literal(text)),
                localizationSource(
                    root(TestTree(root,
                        listOf(
                            TestTree(key, listOf(TestTree(text))),
                            TestTree("key2")
                        )
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isLeaf() ?: false)
        assertEquals(text, property.element?.value())
        assertTrue(property.unresolved.isEmpty())
        assertEquals(property.path, listOf(Literal(root), Literal(key), Literal(text)))
    }

    @Test
    fun resolveObjectByKey() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val root = "root2"
        val key = "key3"
        val value = "key4"
        val property = resolver.resolveCompositeKey(
            listOf(Literal(root), Literal(key)),
                localizationSource(
                    root(
                        TestTree(root,
                            listOf(
                                TestTree(key, listOf(TestTree("subkey2"))),
                                TestTree(value)
                            )
                        )
                    )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
        assertEquals(key, property.element?.value())
        assertTrue(property.unresolved.isEmpty())
        assertEquals(property.path, listOf(Literal(root), Literal(key)))
    }

    @Test
    fun partialResolve() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val root = "root3"
        val key = "key7"
        val sub = "subkey5"
        val property = resolver.resolveCompositeKey(
            listOf(Literal(root), Literal(key), Literal(sub)),
                localizationSource(root(
                    TestTree(root,
                        listOf(
                            TestTree("key5", listOf(TestTree("subkey3"))),
                            TestTree("key6"),
                            TestTree(key, listOf(TestTree("subkey4")))
                        )
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
        assertEquals(key, property.element?.value())
        assertEquals(listOf(Literal(sub)), property.unresolved)
        assertEquals(property.path, listOf(Literal(root), Literal(key)))
    }

    @Test
    fun partialResolve2() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val root = "root4"
        val key = "key11"
        val text = "subkey7"
        val property = resolver.resolveCompositeKey(
            listOf(Literal(root), Literal(key), Literal(text)),
                localizationSource(
                    root(
                        TestTree(root,
                            listOf(
                                TestTree("key8", listOf(TestTree("subkey5"))),
                                TestTree("key9"),
                                TestTree("key10", listOf(TestTree("subkey6")))
                            )
                        )
                    )
                )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
        assertEquals(root, property.element?.value())
        assertEquals(listOf(Literal(key), Literal(text)), property.unresolved)
        assertEquals(property.path, listOf(Literal(root)))
    }

    @Test
    fun stopResolvingAfterFirstFailure2() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val root = "ROOT"
        val key = "Key1"
        val sub = "key3"
        val text = "key31"
        val property = resolver.resolveCompositeKey(
            listOf(Literal(root), Literal(key, 6), Literal(sub, 0), Literal(text, 5)),
            localizationSource(
                root(
                    TestTree(root,
                        listOf(
                            TestTree(key,
                                listOf(
                                    TestTree("key2"),
                                    TestTree(text)
                                )
                            )
                        )
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
        assertEquals(key, property.element?.value())
        assertEquals(listOf(Literal(sub, 0), Literal(text, 5)), property.unresolved)
        assertEquals(listOf(Literal(root), Literal(key, 6)), property.path)
    }

    @Test
    fun resolveProperty() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val main = "main1"
        val sub = "submain1"
        val text = "subsub2"
        val property = resolver.resolveCompositeKeyProperty(
            listOf(Literal(main), Literal(sub), Literal(text)),
            localizationSource(
                root(
                    TestTree(main,
                        listOf(
                            TestTree("submain0", listOf(TestTree("subsub0"))),
                            TestTree(sub, listOf(TestTree("subsub1"), TestTree(text))),
                            TestTree("submain2", listOf(TestTree("subsub3")))
                        )
                    )
                )
            )
        )
        assertTrue(property?.isLeaf() ?: false)
        assertEquals(text, property?.value())
    }
}

internal class CompositeKeyResolverUnresolvedTest {

    private val type = JsonFileType.INSTANCE

    @Test
    fun unresolvedPathIsNull() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val main = "main2"
        val sub = "submain4"
        val property = resolver.resolveCompositeKeyProperty(
            listOf(Literal(main), Literal(sub), Literal("unresolved")),
            localizationSource(
                root(
                    TestTree(main,
                        listOf(
                            TestTree("submain3", listOf(TestTree("subsub4"))),
                            TestTree(sub, listOf(TestTree("subsub5"), TestTree("subsub6"))),
                            TestTree("submain5", listOf(TestTree("subsub7")))
                        )
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
            localizationSource(
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
        )
        assertNull(property)
    }

    @Test
    fun wholeKeyUnresolved() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val root = "root6"
        val key = "key12"
        val sub = "subkey8"
        val property = resolver.resolveCompositeKey(
            listOf(Literal(root), Literal(key), Literal(sub)),
            localizationSource(
                root(
                    TestTree("root5",
                        listOf(
                            TestTree(key),
                            TestTree("key13"),
                            TestTree("key14")
                        )
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
        assertEquals(listOf(Literal(root), Literal(key), Literal(sub)), property.unresolved)
        assertTrue(property.path.isEmpty())
    }

    @Test
    fun stopResolvingAfterFirstFailure() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val root = "root8"
        val key = "key15"
        val subkey = "subkey9"
        val property = resolver.resolveCompositeKey(
            listOf(Literal(root), Literal(key), Literal(subkey)),
            localizationSource(
                root(
                    TestTree("root7",
                        listOf(
                            TestTree(key, listOf(TestTree(subkey))),
                            TestTree("key16"),
                            TestTree("key17", listOf(TestTree("subkey6")))
                        )
                    )
                )
            )
        )
        assertNotNull(property.element)
        assertTrue(property.element?.isTree() ?: false)
        assertEquals("", property.element?.value())
        assertEquals(listOf(Literal(root), Literal(key), Literal(subkey)), property.unresolved)
        assertTrue(property.path.isEmpty())
    }

    @Test
    fun resolvesToEmptyElementOnEmptyTree() {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String>{}
        val property = resolver.resolveCompositeKey(
            listOf(Literal("root9"), Literal("key18"), Literal("subkey10")),
            localizationSource(null)
        )
        assertNull(property.element)
        assertEquals(listOf(Literal("root9"), Literal("key18"), Literal("subkey10")), property.unresolved)
        assertTrue(property.path.isEmpty())
    }
}