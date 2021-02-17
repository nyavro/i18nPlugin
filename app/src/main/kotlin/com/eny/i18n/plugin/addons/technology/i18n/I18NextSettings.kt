package com.eny.i18n.plugin.addons.technology.i18n

import com.eny.i18n.plugin.utils.default
import com.eny.i18n.plugin.utils.whenMatches
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * I18Next settings
 */
@State(name = "i18NextSettings", storages = [Storage("i18nSettings.xml")])
class I18NextSettings : PersistentStateComponent<I18NextSettings> {

    var nsSeparator = ":"
    var keySeparator = "."
    var pluralSeparator = "-"
    var defaultNs = "translation"

    override fun loadState(state: I18NextSettings) = XmlSerializerUtil.copyBean(state, this)

    override fun getState(): I18NextSettings = this

    private val MAX_DEFAULT_NAMESPACES = 100

    /**
     * Gets list of default namespaces
     */
    fun defaultNamespaces(): List<String> =
        defaultNs
            .whenMatches {it.isNotBlank()}
            .default("translation")
            .split("[;|,\\s]".toRegex())
            .filter{it.isNotBlank()}
            .take(MAX_DEFAULT_NAMESPACES)

    /**
     * Service class for persisting settings
     */
    companion object Persistence {
        /**
         * Loads project's Settings instance
         */
        fun getInstance(project: Project): I18NextSettings = ServiceManager.getService(project, I18NextSettings::class.java)
    }
}