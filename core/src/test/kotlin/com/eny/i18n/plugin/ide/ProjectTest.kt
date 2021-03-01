package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.addons.technology.vue.VueSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test

class ProjectTest: BasePlatformTestCase() {

    @Test
    fun testGetService() {
        val settings = myFixture.project.getService(VueSettings::class.java)
        assertNotNull(settings)
    }
}