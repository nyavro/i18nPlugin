package com.eny.i18n.plugin.addons.technology.i18n

import com.eny.i18n.plugin.ide.annotator.CustomSettings
import com.eny.i18n.plugin.ide.annotator.I18NextSettings
import com.intellij.openapi.project.Project
import javax.swing.JComponent

/**
 * PlainObject configurable
 */
class I18NextConfigurable(val project: Project) : CustomSettings() {

    private var settingsPanel: I18NextSettingsFormFragment? = null

    override fun createComponent(): JComponent? {
        settingsPanel = I18NextSettingsFormFragment(I18NextSettings.getInstance(project))
        return settingsPanel?.getRootPanel()
    }

    override fun isModified(): Boolean = settingsPanel?.isModified() ?: false

    override fun apply() {}

    override fun disposeUIResources() {
        settingsPanel = null
    }
}