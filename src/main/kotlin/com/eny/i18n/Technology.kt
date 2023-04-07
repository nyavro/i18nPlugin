package com.eny.i18n

import com.intellij.openapi.project.Project

data class TranslationFunction(val name: String, val argumentIndex: Int)

interface Technology {
    fun translationFunctions(): List<TranslationFunction>
    fun findSourcesByConfiguration(project: Project): List<LocalizationSource>
    fun initialize(project: Project)
    fun cfgNamespaces(): List<String>
}
