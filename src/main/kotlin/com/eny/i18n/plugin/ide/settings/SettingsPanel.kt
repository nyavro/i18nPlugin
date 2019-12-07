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

class SettingsPanel(val settings: Settings, val project: Project) {

    fun getRootPanel(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        root.add(DefaultComponentFactory.getInstance().createSeparator("Settings"), BorderLayout.NORTH)
        root.add(settingsPanel(), BorderLayout.WEST)
        return root
    }

    private fun searchInProjectsOnly(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        val searchInProjectOnly = JCheckBox("Search in project files only", settings.searchInProjectOnly)
        searchInProjectOnly.addItemListener {event -> settings.searchInProjectOnly = searchInProjectOnly.isSelected}
        panel.add(searchInProjectOnly)
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
        panel.add(searchInProjectsOnly())
        panel.add(separator("Namespace separator", settings::nsSeparator))
        panel.add(separator("Key separator", settings::keySeparator))
        panel.add(separator("Plural separator", settings::pluralSeparator))
        panel.add(textInput("Default namespace", settings::defaultNs))
        panel.add(textInput("Stop characters", settings::stopCharacters))
        panel.add(vue())
        panel.add(textInput("Vue locales directory", settings::vueDirectory))
        return panel
    }
}