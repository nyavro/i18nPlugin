package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.quickfix.*
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.FullKey
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
    fun tryToResolveTranslationFile(project:Project, i18nKey:FullKey, source: String, editor:Editor, onComplete: () -> Unit) {
        val search = LocalizationSourceSearch(project)
        val settings = Settings.getInstance(project)
        val files = search.findFilesByName(i18nKey.ns?.text)
        val quickFix = if (files.isEmpty()) {
            val contentGenerator = if (settings.preferYamlFilesGeneration) YamlContentGenerator() else JsonContentGenerator()
            val folderSelector = if (settings.vue) Vue18nTranslationFolderSelector(project) else I18NextTranslationFolderSelector(project)
            val fileName = if (settings.vue) "en" else (i18nKey.ns?.text ?: settings.defaultNs)
            CreateTranslationFileQuickFix(i18nKey, contentGenerator, folderSelector, fileName, source)
        } else {
            val generators = listOf(
                ContentGeneratorAdapter(YamlContentGenerator(), YamlPsiContentGenerator()),
                ContentGeneratorAdapter(JsonContentGenerator(), JsonPsiContentGenerator())
            )
            CreateKeyQuickFix(i18nKey, UserChoice(),  PluginBundle.getMessage("quickfix.create.key"), generators, source, onComplete)
        }
        quickFix.invoke(project, editor)
    }
}
