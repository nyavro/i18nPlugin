package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.factory.CustomSettings
import com.intellij.openapi.project.Project
import javax.swing.JComponent

/**
 * Vue configurable
 */
class VueConfigurable(val project: Project) : CustomSettings() {

    private var settingsPanel: VueSettingsFormFragment? = null

    override fun createComponent(): JComponent? {
        settingsPanel = VueSettingsFormFragment(VueSettings.getInstance(project))
        return settingsPanel?.getRootPanel()
    }

    override fun isModified(): Boolean = settingsPanel?.isModified() ?: false

    override fun apply() {}

    override fun disposeUIResources() {
        settingsPanel = null
    }
}

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

/**
 * PlainObject configurable
 */
class PoConfigurable(val project: Project) : CustomSettings() {

    private var settingsPanel: PlainObjectSettingsFormFragment? = null

    override fun createComponent(): JComponent? {
        settingsPanel = PlainObjectSettingsFormFragment(PoSettings.getInstance(project))
        return settingsPanel?.getRootPanel()
    }

    override fun isModified(): Boolean = settingsPanel?.isModified() ?: false

    override fun apply() {}

    override fun disposeUIResources() {
        settingsPanel = null
    }
}

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