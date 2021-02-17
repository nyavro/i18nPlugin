package com.eny.i18n.plugin.ide.settings

import javax.swing.JPanel

interface SettingsFormFragment<T> {
    fun getRootPanel(): JPanel
    fun isModified(): Boolean
}