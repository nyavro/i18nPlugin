package com.eny.i18n.plugin.quickfix

import com.eny.i18n.plugin.utils.CompositeKeyResolver
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.JsonSearchUtil
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.icons.AllIcons
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class CreatePropertyQuickFix(val fullKey: FullKey) : BaseIntentionAction(), CompositeKeyResolver {

//    private const

    override fun getFamilyName(): String = "i18n resource"

    override fun getText(): String = "Create key"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        ApplicationManager.getApplication().invokeLater(object : Runnable {
            override fun run() {
                if (fullKey.ns != null && editor != null) {
                    val search = JsonSearchUtil(project)
                    val jsonFiles = search.findFilesByName(fullKey.ns.text)
                    if (jsonFiles.size == 1) {
                        createPropertyInFile(project, jsonFiles.get(0))
                    } else if (jsonFiles.size > 1) {
                        chooseFile(project, editor, jsonFiles)
                    }
                }
            }
        })
    }

    private fun createPropertyInFile(project: Project, target: PsiFile): Unit {
        val ref = resolveCompositeKey(fullKey.compositeKey, target)
//        if (ref.element != null) {
//            CommandProcessor.getInstance().executeCommand(project, {
//                    createPropertiesChain(project, ref.element, ref.unresolved)
//                })
//            }, "Create i18n property", UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION)
//        }
    }

    private fun createPropertiesChain(project: Project, element: PsiElement, unresolved: List<String>) {
        if (unresolved.isNotEmpty()) {
//            PsiElementFactory.getInstance(project).createFileFromText(unresolved.get(0), element.getFi)
//            element.add(PsiElementF)
        }
    }

    private fun chooseFile(project: Project, editor: Editor, files: List<PsiFile>): Unit {
        val menu = JBPopupMenu()
        files.forEach {
            file ->
                val menuItem = JBMenuItem(file.containingDirectory.name + '/' + file.name, AllIcons.Json.Object)
                menuItem.addChangeListener({ e ->
                    createPropertyInFile(project, file)
                })
                menu.add(menuItem)
        }
        val position = editor.visualPositionToXY(editor.caretModel.visualPosition)
        menu.show(editor.component, position.x, position.y)
    }
}
