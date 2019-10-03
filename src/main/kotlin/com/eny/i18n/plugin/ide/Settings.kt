package com.eny.i18n.plugin.ide

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "i18nSettings")
class Settings : PersistentStateComponent<Settings> {

    private var word: String = ""

    fun setWord(w: String) {
        this.word = w
    }

    override fun loadState(state: Settings) = XmlSerializerUtil.copyBean(state, this)

    override fun getState(): Settings? = this

    companion object Persistence {
        fun getInstance(project: Project): Settings = ServiceManager.getService(project, Settings::class.java)
    }
}
