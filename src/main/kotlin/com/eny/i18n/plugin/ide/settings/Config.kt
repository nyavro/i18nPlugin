package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.utils.default
import com.eny.i18n.plugin.utils.whenMatches
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope

/**
 * Configuration holder
 */
data class Config (
    //i18next
    val nsSeparator: String = ":",
    val keySeparator: String = ".",
    val pluralSeparator: String = "-",
    val defaultNs: String = "translation",
    //common
    val searchInProjectOnly: Boolean = true,
    val foldingEnabled: Boolean = false,
    val foldingPreferredLanguage: String = "en",
    val foldingMaxLength: Int = 20,
    val extractSorted: Boolean = false,
    val partialTranslationInspectionEnabled: Boolean = false,
    //Yaml
    val preferYamlFilesGeneration: Boolean = false,
    val yamlContentGenerationEnabled: Boolean = true,
    //Po
    val jsonContentGenerationEnabled: Boolean = true,
    val gettext: Boolean = false,
    val gettextAliases: String = "gettext,_,__",
    //Other
    val jsConfiguration: String = ""
) {

    private val MAX_DEFAULT_NAMESPACES = 100

    /**
     * Gets list of default namespaces
     */
    fun defaultNamespaces(): List<String> =
        defaultNs
            .whenMatches {it.isNotBlank()}
            .default(Config().defaultNs)
            .split("[;|,\\s]".toRegex())
            .filter{it.isNotBlank()}
            .take(MAX_DEFAULT_NAMESPACES)

    /**
     * Gets project's search scope
     */
    fun searchScope(project: Project): GlobalSearchScope =
        if (this.searchInProjectOnly) GlobalSearchScope.projectScope(project)
        else GlobalSearchScope.allScope(project)
}