package com.eny.i18n.plugin

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class PlatformBaseTest: BasePlatformTestCase() {

    @BeforeEach
    fun setFixtureUp() {
        setUp()
    }

    @AfterEach
    fun tearFixtureDown() {
        tearDown()
    }
}