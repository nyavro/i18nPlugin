package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.factory.TranslationExtractor
import com.eny.i18n.plugin.ide.quickfix.*
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.localization.json.JsonLocalizationFactory
import com.eny.i18n.plugin.localization.yaml.YamlLocalizationFactory
import com.eny.i18n.plugin.utils.LocalizationSourceSearch
import com.eny.i18n.plugin.utils.PluginBundle
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project

/**
 * Extracts translation key
 */
class KeyCreator {

    /**
     * Tries to resolve translation file
     */
    fun createKey(project:Project, i18nKey: FullKey, source: String, editor:Editor, extractor: TranslationExtractor, onComplete: () -> Unit) {
        val search = LocalizationSourceSearch(project)
        val settings = Settings.getInstance(project)
        val config = settings.config()
        val files = search.findSources(i18nKey.allNamespaces())
        val generators = settings.mainFactory().contentGenerators()
        val quickFix = if (files.isEmpty()) {
            val contentGenerator = if (config.preferYamlFilesGeneration)
                YamlLocalizationFactory().contentGenerator() else
                JsonLocalizationFactory().contentGenerator()
            val fileName = i18nKey.ns?.text ?: config.defaultNamespaces().first()
            CreateTranslationFileQuickFix(i18nKey, contentGenerator, extractor.folderSelector(), fileName, source, onComplete)
        } else {
            CreateKeyQuickFix(i18nKey, UserChoice(), PluginBundle.getMessage("quickfix.create.key"), generators, source, onComplete)
        }
        quickFix.invoke(project, editor)
    }
}
