package com.eny.i18n.plugin.addons.technology.vue

import com.eny.i18n.plugin.addons.yaml.YamlSettings
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.yaml.runYamlConfig
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

fun vue(): (CodeInsightTestFixture, () -> Unit) -> Unit {
    return { fixture, block ->
        fixture.runVueConfig {
            block()
        }
    }
}