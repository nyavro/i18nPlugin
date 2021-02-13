package com.eny.i18n.plugin.yaml

import com.eny.i18n.plugin.addons.yaml.YamlSettings
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

fun preferYamlGeneration(): (CodeInsightTestFixture, () -> Unit) -> Unit {
    return { fixture, block ->
        fixture.runYamlConfig(Pair(YamlSettings::preferYamlFilesGeneration, true)) {
            block()
        }
    }
}