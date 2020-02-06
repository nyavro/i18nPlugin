package com.eny.i18n.plugin.ide.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Project configurable
 */
class Configurable(val project: Project) : SearchableConfigurable {

    private var gui: JPanel? = null

    override fun createComponent(): JComponent {
        gui = SettingsPanel(Settings.getInstance(project), project).getRootPanel()
        return gui!!
    }

    @Nls
    override fun getDisplayName(): String = "i18n Plugin"

    override fun getHelpTopic(): String? = "preference.i18nPlugin"

    override fun getId(): String = "preference.i18nPlugin"

    override fun isModified(): Boolean = false

    override fun apply() {
    }

    override fun disposeUIResources() {
        gui = null
    }
}