package com.eny.i18n.plugin.addons.technology

import com.eny.i18n.plugin.ide.annotator.CustomSettings
import com.eny.i18n.plugin.ide.annotator.CommonSettings
import com.eny.i18n.plugin.ide.settings.CommonSettingsFormFragment
import com.intellij.openapi.project.Project
import javax.swing.JComponent

/**
 * Common configurable
 */
class CommonConfigurable(val project: Project) : CustomSettings() {

    private var settingsPanel: CommonSettingsFormFragment? = null

    override fun createComponent(): JComponent? {
        settingsPanel = CommonSettingsFormFragment(CommonSettings.getInstance(project))
        return settingsPanel?.getRootPanel()
    }

    override fun isModified(): Boolean = settingsPanel?.isModified() ?: false

    override fun apply() {}

    override fun disposeUIResources() {
        settingsPanel = null
    }
}