package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.utils.default
import com.eny.i18n.plugin.utils.whenMatches
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Plugin settings
 */
@State(name = "i18nSettings", storages = [Storage("i18nSettings.xml")])
class Settings : PersistentStateComponent<Settings> {

    var searchInProjectOnly = true

    var nsSeparator = ":"

    var keySeparator = "."

    var pluralSeparator = "-"

    var defaultNs = "translation"

    var vue = false

    var vueDirectory = "locales"

    var jsConfiguration = ""
    
    var stopCharacters = "/"

    var preferYamlFilesGeneration = false

    var foldingEnabled = false

    var foldingPreferredLanguage = "en"

    var foldingMaxLength = 20

    var jsonContentGenerationEnabled = true

    var yamlContentGenerationEnabled = true

    fun defaultNamespaces(): List<String> =
        defaultNs.whenMatches {it.isNotBlank()}.default("translation").split("[;|,\\s]".toRegex()).filter {it.isNotBlank()}

    override fun loadState(state: Settings) = XmlSerializerUtil.copyBean(state, this)

    override fun getState(): Settings? = this

    /**
     * Service class for persisting settings
     */
    companion object Persistence {
        /**
         * Loads project's Settings instance
         */
        fun getInstance(project: Project): Settings = ServiceManager.getService(project, Settings::class.java)
    }
}
