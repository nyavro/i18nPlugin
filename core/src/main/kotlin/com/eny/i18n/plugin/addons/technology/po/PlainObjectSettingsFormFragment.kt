package com.eny.i18n.plugin.addons.technology.po

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
class PlainObjectSettingsFormFragment(val settings: PoSettings): SettingsFormFragment<PoSettings> {

    private val formUtils = FormUtils()

    private val modificationCheck = ModificationCheck<PoSettings>()

    private val initialHash = modificationCheck.hash(settings)

    override fun isModified(): Boolean = initialHash == modificationCheck.hash(settings)

    /**
     * Returns Settings main panel
     */
    override fun getRootPanel(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        root.add(DefaultComponentFactory.getInstance().createSeparator("Plain Object Settings"), BorderLayout.NORTH)
        root.add(settingsPanel(), BorderLayout.WEST)
        return root
    }

    private fun settingsPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(formUtils.checkbox(PluginBundle.getMessage("settings.gettext.enabled"), settings::gettext))
        panel.add(formUtils.textInput(PluginBundle.getMessage("settings.gettext.aliases"), settings::gettextAliases))
        return panel
    }
}