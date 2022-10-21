package com.eny.i18n.plugin

import com.intellij.openapi.application.ApplicationManager
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

    fun read(block: () -> Unit) = ApplicationManager.getApplication().runReadAction(block)

}
