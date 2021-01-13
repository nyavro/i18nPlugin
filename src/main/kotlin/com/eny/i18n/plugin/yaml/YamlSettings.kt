package com.eny.i18n.plugin.yaml

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

fun Project.yamlSettings() = YamlSettings.getInstance(this)
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