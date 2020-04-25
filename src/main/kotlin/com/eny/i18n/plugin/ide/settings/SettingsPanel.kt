package com.eny.i18n.plugin.ide.settings

import com.intellij.openapi.project.Project
import com.jgoodies.forms.factories.DefaultComponentFactory
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import kotlin.reflect.KMutableProperty0

/**
 * JTextField with custom validation
 */
class LimitedTextField(initialText: String, maxLength: Int, onChange: (newText:String) -> Unit, isValid: (key:Char) -> Boolean = {ch -> true}): JTextField(initialText) {
    init {
        addKeyListener(object: KeyAdapter() {
            override fun keyTyped(e: KeyEvent) {
                if (text.length - (if (selectedText==null) 0 else selectedText.length) >= maxLength ||
                    !isValid(e.keyChar)) {
                    e.consume()
                }
            }
        })
        getDocument().addDocumentListener(object : DocumentListener {
            override fun changedUpdate(e: DocumentEvent?) = onChange(text)
            override fun insertUpdate(e: DocumentEvent?) = onChange(text)
            override fun removeUpdate(e: DocumentEvent?) = onChange(text)
        })
    }
}

/**
 * Settings configuration panel
 */
class SettingsPanel(val settings: Settings, val project: Project) {

    /**
     * Returns Settings main panel
     */
    fun getRootPanel(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        root.add(DefaultComponentFactory.getInstance().createSeparator("Settings"), BorderLayout.NORTH)
        root.add(settingsPanel(), BorderLayout.WEST)
        return root
    }
//
//    private fun searchInProjectsOnly(): JPanel {
//        val panel = JPanel()
//        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
//        val checkbox = JCheckBox("Search in project files only", settings.searchInProjectOnly)
//        checkbox.addItemListener { event -> settings.searchInProjectOnly = checkbox.isSelected}
//        panel.add(checkbox)
//        return panel
//    }

    private fun checkbox(label:String, property: KMutableProperty0<Boolean>): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        val checkbox = JCheckBox(label, property.get())
        checkbox.addItemListener { event -> property.set(checkbox.isSelected) }
        panel.add(checkbox)
        return panel
    }

    private fun separator(label:String, property: KMutableProperty0<String>):JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(JLabel(label), BorderLayout.WEST)
        val control = LimitedTextField(property.get(), 1, {txt->property.set(txt)}, {ch -> !listOf(' ', '$', '{', '}', '`').contains(ch)})
        control.preferredSize = Dimension(control.preferredSize.height, control.preferredSize.height)
        panel.add(control, BorderLayout.EAST)
        return panel
    }

    private fun textInput(label:String, property: KMutableProperty0<String>):JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(JLabel(label), BorderLayout.WEST)
        val control = LimitedTextField(property.get(), 100, {txt->property.set(txt)}, {ch -> !listOf(' ', '$', '{', '}', '`').contains(ch)})
        panel.add(control, BorderLayout.EAST)
        return panel
    }

    private fun numberInput(label:String, property: KMutableProperty0<Int>):JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(JLabel(label), BorderLayout.WEST)
        val control = LimitedTextField(property.get().toString(), 2, {txt->property.set(txt.toInt())}, {ch -> ('0'..'9').contains(ch)})
        panel.add(control, BorderLayout.EAST)
        return panel
    }

    private fun vue(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        val vueMode = JCheckBox("Vue-i18n", settings.vue)
        vueMode.addItemListener { event -> settings.vue = vueMode.isSelected}
        panel.add(vueMode)
        return panel
    }

    private fun settingsPanel(): JPanel {
        val panel = JPanel()
        panel.layout = GridLayout(20, 1)
        panel.add(checkbox("Search in project files only", settings::searchInProjectOnly))
        panel.add(separator("Namespace separator", settings::nsSeparator))
        panel.add(separator("Key separator", settings::keySeparator))
        panel.add(separator("Plural separator", settings::pluralSeparator))
        panel.add(textInput("Default namespace", settings::defaultNs))
        panel.add(checkbox("Enable folding", settings::foldingEnabled))
        panel.add(textInput("Preferred folding language", settings::foldingPreferredLanguage))
        panel.add(numberInput("Folding max length", settings::foldingMaxLength))
        panel.add(vue())
        panel.add(textInput("Vue locales directory", settings::vueDirectory))
//        panel.add(checkbox("Prefer YAML translation files", settings::preferYamlFilesGeneration))
//        panel.add(textInput("Custom translation function", settings::translationFunction))
        return panel
    }
}