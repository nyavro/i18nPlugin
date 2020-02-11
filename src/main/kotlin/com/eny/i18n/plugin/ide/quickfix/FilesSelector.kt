package com.eny.i18n.plugin.ide.quickfix

import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu
import com.intellij.psi.PsiFile

/**
 * Files selector interface
 */
interface FilesSelector {

    /**
     * Selects files from given range
     */
    fun select(files: List<PsiFile>, onSelect: (file: PsiFile) -> Unit, editor: Editor)
}

/**
 * Selects and processes all files
 */
class AllFilesSelector : FilesSelector {
    override fun select(files: List<PsiFile>, onSelect: (file: PsiFile) -> Unit, editor: Editor) {
        files.forEach {
            file -> onSelect(file)
        }
    }
}

/**
 * Chooses file from menu and processes it
 */
class UserChoice: FilesSelector {
    override fun select(files: List<PsiFile>, onSelect: (file: PsiFile) -> Unit, editor: Editor) {
        val menu = JBPopupMenu()
        files.forEach {
            file ->
            val menuItem = JBMenuItem(file.containingDirectory.name + '/' + file.name, AllIcons.Json.Object)
            menuItem.addActionListener {
                onSelect(file)
            }
            menu.add(menuItem)
        }
        val position = editor.visualPositionToXY(editor.caretModel.visualPosition)
        menu.show(editor.component, position.x, position.y)
    }
}