package utils

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
