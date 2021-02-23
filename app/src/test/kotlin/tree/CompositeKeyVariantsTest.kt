package tree

import com.eny.i18n.plugin.factory.LocalizationType
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.key.CompositeKeyResolver
import com.intellij.json.JsonFileType
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * CompositeKeyResolver tests
 */
internal class CompositeKeyVariantsTest {

    /**
     * main.acti        -> activity
     * main.activity.   -> swimming
     * main.activity    -> swimming
     * main.place.ff    ->
     * main.place.      -> jungle, forest, sea
     * main.place.fo    -> forest
     */
    fun check(request: String, fixed: List<String>, expected: Set<String>) {
        val resolver: CompositeKeyResolver<String> = object: CompositeKeyResolver<String> {}
        val variants = resolver.listCompositeKeyVariants(
            fixed.map{Literal(it)},
            testTree(),
            request,
            LocalizationType(listOf(JsonFileType.INSTANCE), "test")
        )
        assertEquals(expected, variants.map {it.value()}.toSet())
    }

    @Test
    fun testRunRequests() {
        check("acti", listOf("main"), setOf("activity"))
        check("", listOf("main", "activity"), setOf("swimming"))
        check("activity", listOf("main"), setOf("activity"))
        check("", listOf("main", "place"), setOf("jungle", "forest", "sea"))
        check("fo", listOf("main", "place"), setOf("forest"))
    }

    /**
     * main
     *   food
     *     fruit
     *   place
     *      jungle
     *      forest
     *      sea
     *   activity
     *      swimming
     */
    private fun testTree(): TestTree {
        return root(
            TestTree("main",
                listOf(
                    TestTree("food",
                        listOf(TestTree("fruit"))),
                    TestTree("place",
                        listOf(TestTree("jungle"), TestTree("forest"), TestTree("sea"))),
                    TestTree("activity",
                        listOf(TestTree("swimming")))
                )
            )
        )
    }

    @Test
    fun testListCompositeKeyVariantsOmitPlurals() {
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