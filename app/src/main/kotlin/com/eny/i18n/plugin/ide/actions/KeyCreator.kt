package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.TranslationExtractor
import com.eny.i18n.plugin.ide.CreateKeyQuickFix
import com.eny.i18n.plugin.ide.CreateTranslationFileQuickFix
import com.eny.i18n.plugin.ide.UserChoice
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.ide.LocalizationSourceSearch
import com.eny.i18n.plugin.ide.PluginBundle
import com.eny.i18n.plugin.addons.technology.vue.vueSettings
import com.eny.i18n.plugin.ide.settings.i18NextSettings
import com.eny.i18n.plugin.ide.settings.mainFactory
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
        val files = search.findSources(i18nKey.allNamespaces())
        val generators = project.mainFactory().contentGenerators()
        val quickFix = if (files.isEmpty()) {
            val contentGenerator = generators.find {it.isPreferred(project)} ?: generators.first()
            //TODO - get rid of hardcoded 'en' value
            val fileName = if (project.vueSettings().vue) "en" else (i18nKey.ns?.text ?: project.i18NextSettings().defaultNamespaces().first())
            CreateTranslationFileQuickFix(i18nKey, contentGenerator, extractor.folderSelector(), fileName, source, onComplete)
        } else {
            CreateKeyQuickFix(i18nKey, UserChoice(), PluginBundle.getMessage("quickfix.create.key"), generators, source, onComplete)
        }
        quickFix.invoke(project, editor)
    }
}
