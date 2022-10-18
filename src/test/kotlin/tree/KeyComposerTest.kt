package tree

import com.eny.i18n.plugin.tree.KeyComposer
import com.eny.i18n.plugin.tree.Separators
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class KeyComposerTest {

    @Test
    fun compose() {
        val composer = object : KeyComposer<String>{}
        assertEquals("sampla:ROOT.key1.key311", composer.composeKey(listOf("key311", "key1", "ROOT", "sampla").reversed()))
    }

    @Test
    fun compose2() {
        val composer = object : KeyComposer<String>{}
        assertEquals("samplb", composer.composeKey(listOf("samplb")))
    }

    @Test
    fun composePlural() {
        val composer = object : KeyComposer<String>{}
        assertEquals("samplc:ROOT.key1.key31", composer.composeKey(listOf("key31-1", "key1", "ROOT", "samplc").reversed()))
    }

    @Test
    fun composeLookLikePlural() {
        val composer = object : KeyComposer<String>{}
        assertEquals("samplz:ROOTz.key1z.key31z-8", composer.composeKey(listOf("key31z-8", "key1z", "ROOTz", "samplz").reversed()))
    }

    @Test
    fun pluralFixDoesntAffectNs() {
        val composer = object : KeyComposer<String>{}
        assertEquals("sample-1:ROOT.key1.key312", composer.composeKey(listOf("key312", "key1", "ROOT", "sample-1").reversed()))
    }

    @Test
    fun alternativeKeySeparator() {
        val composer = object : KeyComposer<String>{}
        val tree = listOf("key313", "key7", "ROOW", "altew").reversed()
        assertEquals("altew:ROOW#key7#key313", composer.composeKey(tree, Separators(":", "#", "-")))
    }

    @Test
    fun alternativeKeySeparator2() {
        val composer = object : KeyComposer<String>{}
        val tree = listOf("altex").reversed()
        assertEquals("altex", composer.composeKey(tree, Separators(":", "$", "-")))
    }

    @Test
    fun alternativeKeySeparator3() {
        val composer = object : KeyComposer<String>{}
        val tree = listOf("key314", "key1", "ROOT", "altez").reversed()
        assertEquals("altez\$ROOT#key1#key314", composer.composeKey(tree, Separators("\$", "#", "-")))
    }

    @Test
    fun substituteDefaultNs() {
        val composer = object : KeyComposer<String>{}
        val tree = listOf("second31", "first1", "BASE", "Common").reversed()
        assertEquals("BASE.first1.second31", composer.composeKey(tree, Separators("\$", ".", "-"), listOf("Common")))
    }

    @Test
    fun substituteDefaultNsMulti() {
        val composer = object : KeyComposer<String>{}
        val tree = listOf("second31", "first1", "BASE", "First").reversed()
        assertEquals("BASE.first1.second31", composer.composeKey(tree, Separators("\$", ".", "-"), listOf("First", "Second")))
    }

    @Test
    fun dropRoot() {
        val composer = object : KeyComposer<String>{}
        val tree = listOf("second31k", "first1k", "BASEk", "Firstk").reversed()
        assertEquals("BASEk.first1k.second31k", composer.composeKey(tree, Separators("\$", ".", "-"), listOf(), true))
    }

    @Test
    fun firstComponentNs() {
        val composer = object : KeyComposer<String>{}
        val tree = listOf("second31n", "first1n", "BASEn", "Firstn").reversed()
        assertEquals("Firstn.BASEn.first1n.second31n", composer.composeKey(tree, Separators("\$", ".", "-"), listOf(), false, true))
    }
}
