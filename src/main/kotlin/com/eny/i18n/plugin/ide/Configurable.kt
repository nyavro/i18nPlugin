package com.eny.i18n.plugin.ide

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls
import javax.swing.JComponent
import javax.swing.JPanel


/**
 * This ProjectConfigurable class appears on Settings dialog,
 * to let user to configure this plugin's behavior.
 */
class Configurable(val project: Project) : SearchableConfigurable {

    internal var gui: JPanel? = null

    override fun createComponent(): JComponent {
        gui = SettingsPanel(Settings.getInstance(project)).getRootPanel()
        return gui!!
    }

    @Nls
    override fun getDisplayName(): String {
        return "i18n Plugin"
    }

    override fun getHelpTopic(): String? {
        return "preference.i18nPlugin"
    }

    override fun getId(): String {
        return "preference.i18nPlugin"
    }

    override fun enableSearch(s: String?): Runnable? {
        return null
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun apply() {
    }

    override fun reset() {

    }

    override fun disposeUIResources() {
        gui = null
    }
}