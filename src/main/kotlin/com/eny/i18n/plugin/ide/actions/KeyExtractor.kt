package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.quickfix.*
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.localization.json.JsonContentGenerator
import com.eny.i18n.plugin.localization.yaml.YamlContentGenerator
import com.eny.i18n.plugin.utils.LocalizationSourceSearch
import com.eny.i18n.plugin.utils.PluginBundle
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project

/**
 * Extracts translation key
 */
class KeyExtractor {

    /**
     * Tries to resolve translation file
     */
    fun tryToResolveTranslationFile(project:Project, i18nKey: FullKey, source: String, editor:Editor, onComplete: () -> Unit) {
        val search = LocalizationSourceSearch(project)
        val settings = Settings.getInstance(project)
        val config = settings.config()
        val files = search.findFilesByName(i18nKey.ns?.text)
        val generators = settings.mainFactory().contentGenerators()
        val quickFix = if (files.isEmpty()) {
            val contentGenerator = if (config.preferYamlFilesGeneration) YamlContentGenerator() else JsonContentGenerator()
            val folderSelector = if (config.vue) Vue18nTranslationFolderSelector(project) else I18NextTranslationFolderSelector(project)
            val fileName = if (config.vue) "en" else (i18nKey.ns?.text ?: config.defaultNamespaces().first())
            CreateTranslationFileQuickFix(i18nKey, contentGenerator, folderSelector, fileName, source, onComplete)
        } else {
            CreateKeyQuickFix(i18nKey, UserChoice(),  PluginBundle.getMessage("quickfix.create.key"), generators, source, onComplete)
        }
        quickFix.invoke(project, editor)
    }
}
