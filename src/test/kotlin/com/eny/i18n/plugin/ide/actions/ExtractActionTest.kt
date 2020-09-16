package com.eny.i18n.plugin.ide.actions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ExtractActionTest {

    @ParameterizedTest
    @ValueSource(ints = [1, 3, 5, 13, 15])
    fun someTst(number: Int) {
        Assertions.assertTrue(number % 2 == 1)
    }

//    @ParameterizedTest
//    @ValueSource(ints = [1, 3, 5, -3, 15, Int.MAX_VALUE], chars = ['\'', '"', '`'])
//    fun someTst2(number: Int, quot: Char) {
//        val s = "" + quot + number + quot
//        Assertions.assertEquals(3, s.length)
//    }
}