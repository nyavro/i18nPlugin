package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.utils.PluginBundle
import com.intellij.openapi.options.BaseConfigurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Project configurable
 */
class Configurable(val project: Project) : BaseConfigurable(), SearchableConfigurable {

    private var gui: JPanel? = null

    override fun createComponent(): JComponent {
        gui = SettingsPanel(Settings.getInstance(project), project).getRootPanel()
        return gui!!
    }

    @Nls
    override fun getDisplayName(): String = PluginBundle.getMessage("app.name")

    override fun getHelpTopic(): String? = "preference.i18nPlugin"

    override fun getId(): String = "preference.i18nPlugin"

    override fun apply() {
    }

    override fun disposeUIResources() {
        gui = null
    }
}