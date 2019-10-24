package tree

import com.eny.i18n.plugin.tree.FlippedTree
import com.eny.i18n.plugin.tree.KeyComposer
import com.jetbrains.rd.util.restOrNull
import junit.framework.Assert.assertEquals
import org.junit.Test

class TestFlippedTree(val list: List<String>): FlippedTree<String> {
    override fun getParent(): FlippedTree<String>? {
        return list.restOrNull()?.let { rest -> TestFlippedTree(rest)}
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
}