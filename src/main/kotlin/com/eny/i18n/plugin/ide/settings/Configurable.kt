package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.extension.Extensions
import com.eny.i18n.plugin.factory.CustomSettings
import com.eny.i18n.plugin.utils.PluginBundle
import com.intellij.openapi.extensions.BaseExtensionPointName
import com.intellij.openapi.options.CompositeConfigurable
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Project configurable
 */
class Configurable(val project: Project) : CompositeConfigurable<CustomSettings>(), SearchableConfigurable, Configurable.WithEpDependencies {

    private var gui: JPanel? = null

    override fun createComponent(): JComponent {
        gui = CompositeSettingsPanel(configurables.mapNotNull {it.createComponent()}).compose()
        return gui!!
    }

    @Nls
    override fun getDisplayName(): String = PluginBundle.getMessage("app.name")

    override fun getHelpTopic(): String = "preference.i18nPlugin"

    override fun getId(): String = "preference.i18nPlugin"

    override fun apply() {
    }

    override fun disposeUIResources() {
        gui = null
    }

    override fun getDependencies(): MutableCollection<BaseExtensionPointName<*>> = mutableListOf(Extensions.SETTINGS)

    override fun createConfigurables(): MutableList<CustomSettings> {
        return Extensions.SETTINGS.getExtensions(project)
    }
}