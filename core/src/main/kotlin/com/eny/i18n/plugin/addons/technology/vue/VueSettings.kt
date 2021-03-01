package com.eny.i18n.plugin.addons.technology.vue

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Utility to get project's vue settings
 */
fun Project.vueSettings() = com.eny.i18n.plugin.addons.technology.vue.VueSettings.getInstance(this)

/**
 * Vue-i18n settings
 */
@State(name = "vue-i18n-settings", storages = [Storage("i18nSettings.xml")])
class VueSettings : PersistentStateComponent<VueSettings> {

    var vue: Boolean = false

    var vueDirectory: String = "locales"

    var jsConfiguration: String = ""

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