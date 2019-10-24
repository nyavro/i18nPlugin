package com.eny.i18n.plugin.ide.settings

import com.jgoodies.forms.factories.DefaultComponentFactory
import net.miginfocom.swing.MigLayout
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JPanel

class SettingsPanel(val settings: Settings) {

    fun getRootPanel(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        root.add(DefaultComponentFactory.getInstance().createSeparator("Settings"))
        root.add(settingsPanel())
        return root
    }

    private fun settingsPanel(): JPanel {
        val panel = JPanel()
        panel.layout = MigLayout("", "[][][grow][][][grow]", "[][][]")
        val searchInProjectOnly = JCheckBox("Search in project files only", settings.searchInProjectOnly)
        searchInProjectOnly.addItemListener {event -> settings.searchInProjectOnly = searchInProjectOnly.isSelected}
        panel.add(searchInProjectOnly, "cell 1 1, alignx trailing")
        return panel
    }
}