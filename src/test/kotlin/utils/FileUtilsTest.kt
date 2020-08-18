package utils

import com.eny.i18n.plugin.utils.distance
import com.eny.i18n.plugin.utils.pathToRoot
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class FileUtilsTest {

    @Test
    fun calculateRelativePathToRoot() {
        assertEquals("\\src\\assets\\lang",
            pathToRoot(
                "C:\\Projects\\react\\test",
            "C:\\Projects\\react\\test\\src\\assets\\lang"
            )
        )
        assertEquals("/src/assets/lang",
            pathToRoot(
                "/Users/user/projects/react/test",
                "/Users/user/projects/react/test/src/assets/lang"
            )
        )
    }

    @Test
    fun doesNotAffectPathIfNotUnderRoot() {
        assertEquals("C:\\Projects\\react\\test\\src\\assets\\lang",
            pathToRoot(
            "C:\\Projects\\react\\somethingWentWrong",
            "C:\\Projects\\react\\test\\src\\assets\\lang"
            )
        )
        assertEquals("/Users/user/projects/react/test/src/assets/lang",
            pathToRoot(
            "/Users/user/projects/react/somethingWentWrong",
            "/Users/user/projects/react/test/src/assets/lang"
            )
        )
    }

    @Test
    fun equalPathsHaveZeroDistance() {
        val path = listOf("src", "test", "kotlin", "utils").joinToString(File.separator)
        assertEquals(0, distance(path, path))
    }

    @Test
    fun nestedPathsDistance() {
        val path = listOf("src", "test", "kotlin", "utils").joinToString(File.separator)
        val sub = listOf("src", "test", "kotlin", "utils", "distance", "sub").joinToString(File.separator)
        assertEquals(2, distance(path, sub))
    }

    @Test
    fun arbitraryPathsDistance() {
        val path = listOf("src", "test", "kotlin", "utils").joinToString(File.separator)
        val sub = listOf("src", "main", "kotlin", "utils", "distance", "sub").joinToString(File.separator)
        assertEquals(8, distance(path, sub))
    }

    @Test
    fun boundaryDistance() {
        assertEquals(0, distance("", ""))
        assertEquals(1, distance("one", ""))
        assertEquals(1, distance("", "one"))
    }
}