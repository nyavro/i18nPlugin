package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.factory.MainFactory
import com.eny.i18n.plugin.language.js.JsLanguageFactory
import com.eny.i18n.plugin.language.jsx.JsxLanguageFactory
import com.eny.i18n.plugin.language.php.PhpLanguageFactory
import com.eny.i18n.plugin.language.vue.VueLanguageFactory
import com.eny.i18n.plugin.utils.default
import com.eny.i18n.plugin.utils.whenMatches
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
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

/**
 * Vue-i18n settings
 */
@State(name = "vue-i18n-settings", storages = [Storage("i18nSettings.xml")])
class VueSettings : PersistentStateComponent<VueSettings> {

    var vue: Boolean = false

    var vueDirectory: String = "locales"

    override fun getState(): VueSettings = this

    override fun loadState(state: VueSettings) = XmlSerializerUtil.copyBean(state, this)

    /**
     * Service class for persisting settings
     */
    companion object Persistence {
        /**
         * Loads project's Settings instance
         */
        fun getInstance(project: Project): VueSettings = ServiceManager.getService(project, VueSettings::class.java)
    }
}

fun Project.vueSettings() = VueSettings.getInstance(this)
fun Project.i18NextSettings() = I18NextSettings.getInstance(this)
fun Project.yamlSettings() = YamlSettings.getInstance(this)
fun Project.poSettings() = PoSettings.getInstance(this)
fun Project.commonSettings() = CommonSettings.getInstance(this)

/**
 * Yaml settings
 */
@State(name = "yamlSettings", storages = [Storage("i18nSettings.xml")])
class YamlSettings : PersistentStateComponent<YamlSettings> {

    var preferYamlFilesGeneration: Boolean = false

    var yamlContentGenerationEnabled: Boolean = true

    override fun loadState(state: YamlSettings) = XmlSerializerUtil.copyBean(state, this)

    override fun getState(): YamlSettings = this

    /**
     * Service class for persisting settings
     */
    companion object Persistence {
        /**
         * Loads project's Settings instance
         */
        fun getInstance(project: Project): YamlSettings = ServiceManager.getService(project, YamlSettings::class.java)
    }
}

/**
 * Common i18n settings
 */
@State(name = "commonSettings", storages = [Storage("i18nSettings.xml")])
class CommonSettings : PersistentStateComponent<CommonSettings> {

    var searchInProjectOnly: Boolean = true

    var foldingEnabled: Boolean = false

    var foldingPreferredLanguage: String = "en"

    var foldingMaxLength: Int = 20

    var extractSorted: Boolean = false

    var partialTranslationInspectionEnabled: Boolean = false

    //Other
    var jsonContentGenerationEnabled: Boolean = true

    var jsConfiguration: String = ""

    override fun loadState(state: CommonSettings) = XmlSerializerUtil.copyBean(state, this)

    override fun getState(): CommonSettings = this

    /**
     * Gets project's search scope
     */
    fun searchScope(project: Project): GlobalSearchScope =
        if (this.searchInProjectOnly) GlobalSearchScope.projectScope(project)
        else GlobalSearchScope.allScope(project)

    /**
     * Service class for persisting settings
     */
    companion object Persistence {
        /**
         * Loads project's Settings instance
         */
        fun getInstance(project: Project): CommonSettings = ServiceManager.getService(project, CommonSettings::class.java)
    }
}

/**
 * Plain Object settings
 */
@State(name = "poSettings", storages = [Storage("i18nSettings.xml")])
class PoSettings : PersistentStateComponent<PoSettings> {

    var gettext: Boolean = false

    var gettextAliases: String = "gettext,_,__"

    override fun loadState(state: PoSettings) = XmlSerializerUtil.copyBean(state, this)

    override fun getState(): PoSettings = this

    /**
     * Service class for persisting settings
     */
    companion object Persistence {
        /**
         * Loads project's Settings instance
         */
        fun getInstance(project: Project): PoSettings = ServiceManager.getService(project, PoSettings::class.java)
    }
}

fun Project.mainFactory(): MainFactory {
    return MainFactory(
        listOf(
            listOf(JsLanguageFactory(), JsxLanguageFactory(), PhpLanguageFactory()),
            if (this.vueSettings().vue) listOf(VueLanguageFactory()) else emptyList()
        ).flatten()
    )
}
