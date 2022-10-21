package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

internal fun CodeInsightTestFixture.runWithConfig (config: Config, block: () -> Unit) {
    val settings = Settings.getInstance(this.project)
    val original = settings.config()
    settings.setConfig(config)
    block()
    settings.setConfig(original)
}
