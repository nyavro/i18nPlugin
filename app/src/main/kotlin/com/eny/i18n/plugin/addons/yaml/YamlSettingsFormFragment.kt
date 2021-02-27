package com.eny.i18n.plugin.addons.yaml

import com.eny.i18n.plugin.ide.settings.FormUtils
import com.eny.i18n.plugin.ide.settings.ModificationCheck
import com.eny.i18n.plugin.ide.settings.SettingsFormFragment
import com.eny.i18n.plugin.ide.PluginBundle
import com.jgoodies.forms.factories.DefaultComponentFactory
import java.awt.BorderLayout
import javax.swing.BoxLayout
import javax.swing.JPanel

/**
 * Settings configuration panel
 */
class YamlSettingsFormFragment(val settings: YamlSettings): SettingsFormFragment<YamlSettings> {

    private val formUtils = FormUtils()

    private val modificationCheck = ModificationCheck<YamlSettings>()

    private val initialHash = modificationCheck.hash(settings)

    override fun isModified(): Boolean = initialHash == modificationCheck.hash(settings)

    /**
     * Returns Settings main panel
     */
    override fun getRootPanel(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        //TODO move
        root.add(DefaultComponentFactory.getInstance().createSeparator("Yaml Settings"), BorderLayout.NORTH)
        root.add(settingsPanel(), BorderLayout.WEST)
        return root
    }

    private fun settingsPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(formUtils.checkbox(PluginBundle.getMessage("settings.prefer.yaml.files.generation"), settings::preferYamlFilesGeneration))
        return panel
    }
}