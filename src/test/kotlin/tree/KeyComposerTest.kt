package tree

import com.eny.i18n.plugin.tree.FlippedTree
import com.eny.i18n.plugin.tree.KeyComposer
import junit.framework.Assert.assertEquals
import org.junit.Test

class TestFlippedTree(val list: List<String>): FlippedTree<String> {
    override fun isRoot() = list.size == 1
    override fun ancestors(): List<FlippedTree<String>> {
        fun parents(lst: List<String>): List<FlippedTree<String>> {
            if (lst.isEmpty()) return listOf()
            else return parents(lst.drop(1)) + TestFlippedTree(lst)
        }
        return parents(list)
    }
    override fun name() = list.first()
}

class KeyComposerTest {

    @Test
    fun compose() {
        val composer = object : KeyComposer<String>{}
        val tree = TestFlippedTree(listOf("key31", "key1", "ROOT", "sample"))
        assertEquals("sample:ROOT.key1.key31", composer.composeKey(tree, ":", "."))
    }

    @Test
    fun compose2() {
        val composer = object : KeyComposer<String>{}
        val tree = TestFlippedTree(listOf("sample"))
        assertEquals("sample", composer.composeKey(tree, ":", "."))
    }
}