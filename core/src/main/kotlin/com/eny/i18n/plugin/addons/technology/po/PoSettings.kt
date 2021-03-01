package com.eny.i18n.plugin.addons.technology.po

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

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