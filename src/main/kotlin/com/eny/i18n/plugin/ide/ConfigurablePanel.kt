package com.eny.i18n.plugin.ide

import com.intellij.ui.components.textFieldWithBrowseButton
import com.intellij.ui.layout.panel
import javax.swing.JComponent
import javax.swing.JTextField

class ConfigurablePanel {
    fun getRootPanel(): JComponent {
        val namespaceField = JTextField()
        return panel {
            noteRow("I18n plugin configuration")
            row("Namespace separator:", namespaceField(settings::namespaceSeparator))
        }
    }
}
