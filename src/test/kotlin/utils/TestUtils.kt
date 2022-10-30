package utils

import com.eny.i18n.plugin.utils.CollectingSequence
import com.eny.i18n.plugin.utils.whenMatches
import com.eny.i18n.plugin.utils.whenMatchesDo
import com.eny.i18n.plugin.utils.whenNotEmpty

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestUtils {

    @Test
    fun whenNotEmptyTest() {
        assertEquals(2, listOf(1,2).whenNotEmpty { it.size })
        assertNull(emptyList<Int>().whenNotEmpty { it.size })
    }

    @Test
    fun whenMatchesTest() {
        assertEquals("TypeScript", "TypeScript".whenMatches { it.startsWith("Type") })
        assertNull("TypeScript".whenMatches { it.startsWith("Java") })
    }

    @Test
    fun whenMatchesDoTest() {
        assertEquals("TypeScript rules!", "TypeScript".whenMatchesDo ({ it.startsWith("Type") }) {it + " rules!"})
        assertEquals("TypeScript", "TypeScript".whenMatchesDo ({it.startsWith("Java")}) {it + " no effect"})
    }
}

class CollectingSequenceTest {

    @Test
    fun testCollectingSequence() {
        assertEquals(
            CollectingSequence(sequenceOf(1, 2, 3, 4, 5)) {if(it % 2 == 0) (it.toString()) else null}.toList(),
            listOf("2", "4")
        )
    }

    @Test
    fun testCollectingSequenceIteration() {
        val collectingSequence = CollectingSequence(sequenceOf(1, 2, 3, 4, 5)) { if (it % 2 == 0) (it.toString()) else null }.iterator()
        assertTrue(collectingSequence.hasNext())
        assertTrue(collectingSequence.hasNext())
        assertEquals("2", collectingSequence.next())
        assertTrue(collectingSequence.hasNext())
        assertEquals("4", collectingSequence.next())
        assertFalse(collectingSequence.hasNext())
    }

    @Test
//        (expected = NoSuchElementException::class)
    fun emptyIteratorNextThrowsException() {
        CollectingSequence(emptySequence<Int>(), {it}).iterator().next()
    }

    @Test
    fun emptyIteratorDoesNotHaveNext() {
        assertFalse(CollectingSequence(emptySequence<Int>(), {it}).iterator().hasNext())
    }
}