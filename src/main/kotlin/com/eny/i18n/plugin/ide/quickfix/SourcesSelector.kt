package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.LocalizationSource
import com.eny.i18n.plugin.utils.distance
import com.eny.i18n.plugin.utils.pathToRoot
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu
import java.io.File

/**
 * Sources selector interface
 */
interface SourcesSelector {

    /**
     * Selects source from given range
     */
    fun select(sources: List<LocalizationSource>, onSelect: (sources: List<LocalizationSource>) -> Unit, editor: Editor)
}

/**
 * Selects and processes all files
 */
class AllSourcesSelector : SourcesSelector {
    override fun select(sources: List<LocalizationSource>, onSelect: (sources: List<LocalizationSource>) -> Unit, editor: Editor) {
        onSelect(sources)
    }
}

/**
 * Chooses file from menu and processes it
 */
class UserChoice: SourcesSelector {

    override fun select(sources: List<LocalizationSource>, onSelect: (sources: List<LocalizationSource>) -> Unit, editor: Editor) {
        val menu = JBPopupMenu()
        val currentFile = pathToRoot(editor.project?.basePath ?: "", FileDocumentManager.getInstance().getFile(editor.document)?.path ?: "").trim(File.separatorChar)
        sources
            .sortedBy {
                //Closest to current file first:
                distance(it.displayPath.trim(File.separatorChar), currentFile)
            }
            .forEach {
                val menuItem = JBMenuItem(it.displayPath, it.localization.icon())
                menuItem.addActionListener { _ ->
                    onSelect(listOf(it))
                }
                menu.add(menuItem)
            }
        val position = editor.visualPositionToXY(editor.caretModel.visualPosition)
        menu.show(editor.component, position.x, position.y)
    }
}