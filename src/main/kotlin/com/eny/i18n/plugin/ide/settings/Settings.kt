package com.eny.i18n.plugin.ide.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "i18nSettings", storages = [Storage("i18nSettings.xml")])
class Settings : PersistentStateComponent<Settings> {

    var searchInProjectOnly = true

    var nsSeparator = ":"

    var keySeparator = "."

    var pluralSeparator = "-"

    var defaultNs = "translation"

    override fun loadState(state: Settings) = XmlSerializerUtil.copyBean(state, this)

    override fun getState(): Settings? = this

    companion object Persistence {
        fun getInstance(project: Project): Settings = ServiceManager.getService(project, Settings::class.java)
    }
}
