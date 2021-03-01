package com.eny.i18n.plugin.yaml

import com.eny.i18n.plugin.addons.yaml.YamlSettings
import com.eny.i18n.plugin.ide.runWithBooleanSettings
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import kotlin.reflect.KMutableProperty1

internal fun CodeInsightTestFixture.runYamlConfig (vararg props: Pair<KMutableProperty1<YamlSettings, Boolean>, Boolean>, block: (YamlSettings) -> Unit) =
        runWithBooleanSettings(mapOf(*props), YamlSettings.getInstance(this.project), block)