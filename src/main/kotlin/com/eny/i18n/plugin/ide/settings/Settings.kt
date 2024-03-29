package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.Extensions
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Plugin settings
 */
@State(name = "i18nSettings", storages = [Storage("i18nSettings.xml")])
class Settings : PersistentStateComponent<Settings> {

    private val default: Config = Config()

    internal var searchInProjectOnly = default.searchInProjectOnly
    internal var nsSeparator = default.nsSeparator
    internal var keySeparator = default.keySeparator
    internal var pluralSeparator = default.pluralSeparator
    internal var defaultNs = default.defaultNs
    internal var firstComponentNs = default.firstComponentNs
    internal var jsConfiguration = default.jsConfiguration
    internal var foldingEnabled = default.foldingEnabled
    internal var foldingPreferredLanguage = default.foldingPreferredLanguage
    internal var foldingMaxLength = default.foldingMaxLength
    internal var extractSorted = default.extractSorted
    internal var gettext = default.gettext
    internal var gettextAliases = default.gettextAliases
    internal var partialTranslationInspectionEnabled = default.partialTranslationInspectionEnabled
    internal var preferredLocalization = default.preferredLocalization
    internal var localizationConfig = default.localizationConfig

    /**
     * Returns plugin configuration
     */
    fun config(): Config {
        if (preferredLocalization.isEmpty()) {
            preferredLocalization = Extensions.LOCALIZATION.extensionList.firstOrNull()?.config()?.id() ?: ""
        }
        return Config(
            searchInProjectOnly = searchInProjectOnly,
            nsSeparator = nsSeparator,
            keySeparator = keySeparator,
            pluralSeparator = pluralSeparator,
            defaultNs = defaultNs,
            firstComponentNs = firstComponentNs,
            jsConfiguration = jsConfiguration,
            foldingEnabled = foldingEnabled,
            foldingPreferredLanguage = foldingPreferredLanguage,
            foldingMaxLength = foldingMaxLength,
            extractSorted = extractSorted,
            gettext = gettext,
            gettextAliases = gettextAliases,
            partialTranslationInspectionEnabled = partialTranslationInspectionEnabled,
            preferredLocalization = preferredLocalization,
            localizationConfig = localizationConfig
        )
    }

    fun setConfig(config: Config) {
        searchInProjectOnly = config.searchInProjectOnly
        nsSeparator = config.nsSeparator
        keySeparator = config.keySeparator
        pluralSeparator = config.pluralSeparator
        defaultNs = config.defaultNs
        firstComponentNs = config.firstComponentNs
        jsConfiguration = config.jsConfiguration
        foldingEnabled = config.foldingEnabled
        foldingPreferredLanguage = config.foldingPreferredLanguage
        foldingMaxLength = config.foldingMaxLength
        extractSorted = config.extractSorted
        gettext = config.gettext
        gettextAliases = config.gettextAliases
        partialTranslationInspectionEnabled = config.partialTranslationInspectionEnabled
        preferredLocalization = config.preferredLocalization
        localizationConfig = config.localizationConfig
    }

    override fun loadState(state: Settings) = XmlSerializerUtil.copyBean(state, this)

    override fun getState(): Settings = this

    /**
     * Service class for persisting settings
     */
    companion object Persistence {
        /**
         * Loads project's Settings instance
         */
        fun getInstance(project: Project): Settings = project.getService(Settings::class.java)
    }
}
