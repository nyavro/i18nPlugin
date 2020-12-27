package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.factory.CustomSettings
import com.intellij.openapi.project.Project
import javax.swing.JComponent

/**
 * Project configurable
 */
class VueConfigurable(val project: Project) : CustomSettings() {

    private var settingsPanel: VueSettingsPanel? = null

    override fun createComponent(): JComponent? {
        settingsPanel = VueSettingsPanel(
            project,
            VueSettings.getInstance(project)
        )
        return settingsPanel?.getRootPanel()
    }

    override fun isModified(): Boolean = settingsPanel?.isModified() ?: false

    override fun apply() {}

    override fun disposeUIResources() {
        settingsPanel = null
    }
}