package tree

import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.utils.Literal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * CompositeKeyResolver tests
 */
internal class CompositeKeyVariantsTest {

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
        val list = listOf("s-tr1", "s-tr2", "str3-1", "str3-2", "str3-5", "str4-1", "str4-2", "str5", "str6-1", "str6-2", "str6-5")
        val grouped = list
            .groupBy {it.substringBeforeLast("-")}
            .entries.flatMap {
                entry -> if(entry.value.size == 3 && entry.value.containsAll(listOf(1,2,5).map{entry.key+"-"+it})) {
                    listOf(entry.key)} else entry.value
            }

        assertEquals(7, grouped.size)
        assertEquals(listOf("s-tr1", "s-tr2", "str3", "str4-1", "str4-2", "str5", "str6"), grouped)
    }
}