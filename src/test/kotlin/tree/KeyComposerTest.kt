package tree

import com.eny.i18n.plugin.tree.FlippedTree
import com.eny.i18n.plugin.tree.KeyComposer
import org.junit.Test
import kotlin.test.assertEquals

internal class TestFlippedTree(val list: List<String>): FlippedTree<String> {
    override fun isRoot() = list.size == 1
    override fun parents(): List<FlippedTree<String>> {
        fun parents(lst: List<String>): List<FlippedTree<String>> {
            if (lst.isEmpty()) return listOf()
            else return parents(lst.drop(1)) + TestFlippedTree(lst)
        }
        return parents(list)
    }
    override fun name() = list.first()
}

internal class KeyComposerTest {

    @Test
    fun compose() {
        val composer = object : KeyComposer<String>{}
        val tree = TestFlippedTree(listOf("key311", "key1", "ROOT", "sampla"))
        assertEquals("sampla:ROOT.key1.key311", composer.composeKey(tree))
    }

    @Test
    fun compose2() {
        val composer = object : KeyComposer<String>{}
        val tree = TestFlippedTree(listOf("samplb"))
        assertEquals("samplb", composer.composeKey(tree))
    }

    @Test
    fun composePlural() {
        val composer = object : KeyComposer<String>{}
        val tree = TestFlippedTree(listOf("key31-1", "key1", "ROOT", "samplc"))
        assertEquals("samplc:ROOT.key1.key31", composer.composeKey(tree))
    }

    @Test
    fun pluralFixDoesntAffectNs() {
        val composer = object : KeyComposer<String>{}
        val tree = TestFlippedTree(listOf("key312", "key1", "ROOT", "sample-1"))
        assertEquals("sample-1:ROOT.key1.key312", composer.composeKey(tree))
    }

    @Test
    fun alternativeKeySeparator() {
        val composer = object : KeyComposer<String>{}
        val tree = TestFlippedTree(listOf("key313", "key7", "ROOW", "altew"))
        assertEquals("altew:ROOW#key7#key313", composer.composeKey(tree, ":", "#"))
    }

    @Test
    fun alternativeKeySeparator2() {
        val composer = object : KeyComposer<String>{}
        val tree = TestFlippedTree(listOf("altez"))
        assertEquals("altez", composer.composeKey(tree, ":", "$"))
    }

    @Test
    fun alternativeKeySeparator3() {
        val composer = object : KeyComposer<String>{}
        val tree = TestFlippedTree(listOf("key314", "key1", "ROOT", "altez"))
        assertEquals("altez\$ROOT#key1#key314", composer.composeKey(tree, "\$", "#"))
    }

    @Test
    fun substituteDefaultNs() {
        val composer = object : KeyComposer<String>{}
        val tree = TestFlippedTree(listOf("second31", "first1", "BASE", "Common"))
        assertEquals("BASE.first1.second31", composer.composeKey(tree, "\$", ".", "-", "Common"))
    }
}