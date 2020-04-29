package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.utils.LocalizationSource
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu

/**
 * Sources selector interface
 */
interface SourcesSelector {

    /**
     * Selects source from given range
     */
    fun select(sources: List<LocalizationSource>, onSelect: (source: LocalizationSource) -> Unit, editor: Editor)
}

/**
 * Selects and processes all files
 */
class AllSourcesSelector : SourcesSelector {
    override fun select(sources: List<LocalizationSource>, onSelect: (source: LocalizationSource) -> Unit, editor: Editor) =
        sources.forEach { onSelect(it) }
}

/**
 * Chooses file from menu and processes it
 */
class UserChoice: SourcesSelector {
    override fun select(sources: List<LocalizationSource>, onSelect: (source: LocalizationSource) -> Unit, editor: Editor) {
        val menu = JBPopupMenu()
        sources.forEach {
            val menuItem = JBMenuItem(it.parent + '/' + it.name, AllIcons.Json.Object)
            menuItem.addActionListener { _ -> onSelect(it) }
            menu.add(menuItem)
        }
        val position = editor.visualPositionToXY(editor.caretModel.visualPosition)
        menu.show(editor.component, position.x, position.y)
    }
}