package com.eny.i18n

import com.eny.i18n.plugin.tree.Tree
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.openapi.fileTypes.FileType

/**
 * Describes localization source.
 * May be root of json, yaml file, js object
 */
data class LocalizationSource(
    val tree: Tree<PsiElement>?,
    val name: String,
    val parent: String,
    val displayPath: String,
    val localization: Localization<PsiElement>,
    val host: PsiElement? = null
)
