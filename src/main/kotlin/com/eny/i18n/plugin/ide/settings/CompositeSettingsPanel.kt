package com.eny.i18n.plugin.ide.settings

import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class CompositeSettingsPanel(val panel: JComponent, val panels: List<JComponent>) {
    fun compose(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        root.add(panel, BorderLayout.PAGE_START)
        panels.forEach {
            root.add(it, BorderLayout.PAGE_END)
        }
        return root
    }
}
