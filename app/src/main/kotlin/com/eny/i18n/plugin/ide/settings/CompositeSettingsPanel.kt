package com.eny.i18n.plugin.ide.settings

import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JPanel

class CompositeSettingsPanel(val panels: List<JComponent>) {
    fun compose(): JPanel {
        val root = JPanel()
        root.layout = BoxLayout(root, BoxLayout.Y_AXIS)// BorderLayout()
        panels.forEach {
            root.add(it)
        }
        return root
    }
}
