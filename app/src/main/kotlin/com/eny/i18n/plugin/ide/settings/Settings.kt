package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.factory.MainFactory
import com.eny.i18n.plugin.addons.js.JsLanguageFactory
import com.eny.i18n.plugin.addons.js.JsxLanguageFactory
import com.eny.i18n.plugin.addons.technology.i18n.I18NextSettings
import com.eny.i18n.plugin.addons.technology.php.PhpLanguageFactory
import com.eny.i18n.plugin.addons.technology.po.PoSettings
import com.eny.i18n.plugin.addons.technology.vue.VueLanguageFactory
import com.eny.i18n.plugin.addons.technology.vue.vueSettings
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.xmlb.XmlSerializerUtil

fun Project.i18NextSettings() = I18NextSettings.getInstance(this)
fun Project.poSettings() = PoSettings.getInstance(this)
fun Project.commonSettings() = CommonSettings.getInstance(this)

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

fun Project.mainFactory(): MainFactory {
    //TODO
    val vue = false //this.vueSettings().vue
    return MainFactory(
        listOf(
            listOf(JsLanguageFactory(), JsxLanguageFactory(), PhpLanguageFactory()),
            if (vue) listOf(VueLanguageFactory()) else emptyList()
        ).flatten()
    )
}
