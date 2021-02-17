package com.eny.i18n.plugin.addons.technology.i18n

import com.eny.i18n.plugin.ide.settings.FormUtils
import com.eny.i18n.plugin.ide.settings.ModificationCheck
import com.eny.i18n.plugin.ide.settings.SettingsFormFragment
import com.eny.i18n.plugin.ide.settings.addLimitationsAndHandlers
import com.eny.i18n.plugin.utils.PluginBundle
import com.jgoodies.forms.factories.DefaultComponentFactory
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import kotlin.reflect.KMutableProperty0

/**
 * Settings configuration panel
 */
class I18NextSettingsFormFragment(val settings: I18NextSettings): SettingsFormFragment<I18NextSettings> {

    private val formUtils = FormUtils()

    private val modificationCheck = ModificationCheck<I18NextSettings>()

    private val initialHash = modificationCheck.hash(settings)

    override fun isModified(): Boolean = initialHash == modificationCheck.hash(settings)

    /**
     * Returns Settings main panel
     */
    override fun getRootPanel(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        root.add(DefaultComponentFactory.getInstance().createSeparator("I18Next Settings"), BorderLayout.NORTH)
        root.add(settingsPanel(), BorderLayout.WEST)
        return root
    }

    private fun separator(label:String, property: KMutableProperty0<String>): JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.preferredSize = Dimension(350, 30)
        panel.add(JLabel(label), BorderLayout.WEST)
        val control = JTextField(property.get())
        control.name = label
        addLimitationsAndHandlers(control, 1, property::set, { !" {}$`".contains(it) })
        control.preferredSize = Dimension(30, 30)
        panel.add(control, BorderLayout.EAST)
        return panel
    }

    private fun settingsPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(separator(PluginBundle.getMessage("settings.namespace.separator"), settings::nsSeparator))
        panel.add(separator(PluginBundle.getMessage("settings.key.separator"), settings::keySeparator))
        panel.add(separator(PluginBundle.getMessage("settings.plural.separator"), settings::pluralSeparator))
        panel.add(formUtils.textArea(PluginBundle.getMessage("settings.default.namespace"), settings::defaultNs))
        return panel
    }
}