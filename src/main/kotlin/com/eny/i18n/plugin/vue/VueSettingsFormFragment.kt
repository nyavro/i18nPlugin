package com.eny.i18n.plugin.vue

import com.eny.i18n.plugin.ide.settings.ModificationCheck
import com.eny.i18n.plugin.ide.settings.SettingsFormFragment
import com.eny.i18n.plugin.ide.settings.addLimitationsAndHandlers
import com.eny.i18n.plugin.utils.PluginBundle
import com.jgoodies.forms.factories.DefaultComponentFactory
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import kotlin.reflect.KMutableProperty0

/**
 * Settings configuration panel
 */
class VueSettingsFormFragment(val settings: VueSettings): SettingsFormFragment<VueSettings> {

    private val modificationCheck = ModificationCheck<VueSettings>()

    private val initialHash = modificationCheck.hash(settings)

    override fun isModified(): Boolean = initialHash == modificationCheck.hash(settings)

    /**
     * Returns Settings main panel
     */
    override fun getRootPanel(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        root.add(DefaultComponentFactory.getInstance().createSeparator("Vue-i18n Settings"), BorderLayout.NORTH)
        root.add(settingsPanel(), BorderLayout.WEST)
        return root
    }

    private fun textInput(label:String, property: KMutableProperty0<String>): JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.preferredSize = Dimension(350, 30)
        panel.add(JLabel(label), BorderLayout.WEST)
        val control = JTextField(property.get())
        control.name = label
        addLimitationsAndHandlers(control, 100, property::set)
        control.preferredSize = Dimension(100, 30)
        panel.add(control, BorderLayout.EAST)
        return panel
    }

    private fun vue(): JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.preferredSize = Dimension(350, 30)
        val label = PluginBundle.getMessage("settings.vue")
        val vueMode = JCheckBox(label, settings.vue)
        vueMode.name = label
        vueMode.addItemListener { _ ->
            settings.vue = vueMode.isSelected
        }
        panel.add(vueMode, BorderLayout.WEST)
        return panel
    }

    private fun settingsPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(vue())
        panel.add(textInput(PluginBundle.getMessage("settings.vue.locales.directory"), settings::vueDirectory))
        return panel
    }
}