package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.factory.MainFactory
import com.eny.i18n.plugin.language.js.JsLanguageFactory
import com.eny.i18n.plugin.language.jsx.JsxLanguageFactory
import com.eny.i18n.plugin.language.php.PhpLanguageFactory
import com.eny.i18n.plugin.language.vue.VueLanguageFactory
import com.eny.i18n.plugin.localization.json.JsonLocalizationFactory
import com.eny.i18n.plugin.localization.yaml.YamlLocalizationFactory
import com.intellij.openapi.application.ApplicationManager
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

    private val default: Config = Config()

    private var searchInProjectOnly = default.searchInProjectOnly

    private var nsSeparator = default.nsSeparator

    private var keySeparator = default.keySeparator

    private var pluralSeparator = default.pluralSeparator

    private var defaultNs = default.defaultNs

    private var vue = default.vue

    private var vueDirectory = default.vueDirectory

    private var jsConfiguration = default.jsConfiguration

    private var preferYamlFilesGeneration = default.preferYamlFilesGeneration

    private var foldingEnabled = default.foldingEnabled

    private var foldingPreferredLanguage = default.foldingPreferredLanguage

    private var foldingMaxLength = default.foldingMaxLength

    private var jsonContentGenerationEnabled = default.jsonContentGenerationEnabled

    private var yamlContentGenerationEnabled = default.yamlContentGenerationEnabled

    fun config(): Config {
        if (ApplicationManager.getApplication().isHeadlessEnvironment) {
            synchronized(this) {
                return doGetConfig()
            }
        } else {
            return doGetConfig()
        }
    }

    private fun doGetConfig() = Config(
        searchInProjectOnly = searchInProjectOnly,
        nsSeparator = nsSeparator,
        keySeparator = keySeparator,
        pluralSeparator = pluralSeparator,
        defaultNs = defaultNs,
        vue = vue,
        vueDirectory = vueDirectory,
        jsConfiguration = jsConfiguration,
        preferYamlFilesGeneration = preferYamlFilesGeneration,
        foldingEnabled = foldingEnabled,
        foldingPreferredLanguage = foldingPreferredLanguage,
        foldingMaxLength = foldingMaxLength,
        jsonContentGenerationEnabled = jsonContentGenerationEnabled,
        yamlContentGenerationEnabled = yamlContentGenerationEnabled
    )

    fun setConfig(config: Config) {
        if (ApplicationManager.getApplication().isHeadlessEnvironment) {
            // Only in Test mode
            synchronized(this) {
                doSetConfig(config)
            }
        } else {
            doSetConfig(config)
        }
    }

    private fun doSetConfig(config: Config) {
        searchInProjectOnly = config.searchInProjectOnly
        nsSeparator = config.nsSeparator
        keySeparator = config.keySeparator
        pluralSeparator = config.pluralSeparator
        defaultNs = config.defaultNs
        vue = config.vue
        vueDirectory = config.vueDirectory
        jsConfiguration = config.jsConfiguration
        preferYamlFilesGeneration = config.preferYamlFilesGeneration
        foldingEnabled = config.foldingEnabled
        foldingPreferredLanguage = config.foldingPreferredLanguage
        foldingMaxLength = config.foldingMaxLength
        jsonContentGenerationEnabled = config.jsonContentGenerationEnabled
        yamlContentGenerationEnabled = config.yamlContentGenerationEnabled
    }

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

    fun mainFactory(): MainFactory =
        MainFactory(
            listOf(
                listOf(JsLanguageFactory(), JsxLanguageFactory(), PhpLanguageFactory()),
                if (this.vue) listOf(VueLanguageFactory()) else emptyList()
            ).flatten(),
            listOf(
                if (this.jsonContentGenerationEnabled) listOf(JsonLocalizationFactory()) else emptyList(),
                if (this.yamlContentGenerationEnabled) listOf(YamlLocalizationFactory()) else emptyList()
            ).flatten()
        )
}
