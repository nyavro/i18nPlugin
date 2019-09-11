import com.eny.i18n.plugin.I18nFullKey
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class I18nFullKeyTest {
    @Test
    fun parse() {
        assertNotNull(I18nFullKey.parse("sample:key1.key2"))
        assertNotNull(I18nFullKey.parse("samplv:root.key4.key5."))
    }

    @Test
    fun invalidFileName() {
        assertNull(I18nFullKey.parse("sam{ple:root.key0"))
    }

    @Test
    fun invalidCompositeKey() {
        assertNull(I18nFullKey.parse("sample:root.key0,key2"))
    }

    @Test
    fun parsesFileName() {
        val fk = I18nFullKey.parse("file123:key1.key2")
        assertEquals(fk?.fileName, "file123")
    }

    @Test
    fun parsesCompositeKey() {
        val fk = I18nFullKey.parse("file:key1.key2.key3")
//        assertEquals(fk?.compositeKey, listOf("key1", "key2", "key3"))
    }
}