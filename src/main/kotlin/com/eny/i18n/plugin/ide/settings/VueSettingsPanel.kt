package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.utils.PluginBundle
import com.intellij.openapi.project.Project
import com.jgoodies.forms.factories.DefaultComponentFactory
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import kotlin.reflect.KMutableProperty0

/**
 * Settings configuration panel
 */
class VueSettingsPanel(val project: Project, val vueSettings: VueSettings) {

    private fun hash(settings: VueSettings): String = "vue:${settings.vue},vueDirectory:${settings.vueDirectory}"

    private val initialHash = hash(vueSettings)

    /**
     * Returns Settings main panel
     */
    fun getRootPanel(): JComponent {
        val root = JPanel()
        root.layout = BorderLayout()
        root.add(DefaultComponentFactory.getInstance().createSeparator("Vue-i18n Settings"), BorderLayout.NORTH)
        root.add(settingsPanel(), BorderLayout.WEST)
        return root
    }

    private fun textInput(label:String, property: KMutableProperty0<String>):JPanel {
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
        val vueMode = JCheckBox(label, vueSettings.vue)
        vueMode.name = label
        vueMode.addItemListener { _ ->
            vueSettings.vue = vueMode.isSelected
        }
        panel.add(vueMode, BorderLayout.WEST)
        return panel
    }

    private fun settingsPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(vue())
        panel.add(textInput(PluginBundle.getMessage("settings.vue.locales.directory"), vueSettings::vueDirectory))
        return panel
    }

    fun isModified(): Boolean = initialHash != hash(vueSettings)
}