package com.eny.i18n.plugin.yaml

import com.eny.i18n.plugin.factory.CustomSettings
import com.intellij.openapi.project.Project
import javax.swing.JComponent

/**
 * Yaml configurable
 */
class YamlConfigurable(val project: Project) : CustomSettings() {

    private var settingsPanel: YamlSettingsFormFragment? = null

    override fun createComponent(): JComponent? {
        settingsPanel = YamlSettingsFormFragment(YamlSettings.getInstance(project))
        return settingsPanel?.getRootPanel()
    }

    override fun isModified(): Boolean = settingsPanel?.isModified() ?: false

    override fun apply() {}

    override fun disposeUIResources() {
        settingsPanel = null
    }
}