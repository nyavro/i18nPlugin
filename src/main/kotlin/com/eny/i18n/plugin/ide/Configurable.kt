package com.eny.i18n.plugin.ide

import javax.swing.JComponent
import org.jetbrains.annotations.Nls
import com.intellij.openapi.options.SearchableConfigurable


/**
 * This ProjectConfigurable class appears on Settings dialog,
 * to let user to configure this plugin's behavior.
 */
class Configurable : SearchableConfigurable {

    internal var gui: ConfigurablePanel? = null

    override fun createComponent(): JComponent {
        gui = ConfigurablePanel()
        return gui!!.getRootPanel()
    }

    @Nls
    override fun getDisplayName(): String {
        return "i18n Plugin"
    }

    override fun getHelpTopic(): String? {
        return "preference.i18nPlugin"
    }

    override fun getId(): String {
        return "preference.i18nPlugin"
    }

    override fun enableSearch(s: String?): Runnable? {
        return null
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun apply() {
    }

    override fun reset() {

    }

    override fun disposeUIResources() {
        gui = null
    }
}