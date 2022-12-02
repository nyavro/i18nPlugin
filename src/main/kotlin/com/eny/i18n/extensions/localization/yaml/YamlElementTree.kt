package com.eny.i18n.extensions.localization.yaml

import com.eny.i18n.plugin.tree.Tree
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLMapping

/**
 * Tree wrapper around yaml psi tree
 */
class YamlElementTree(val element: PsiElement): Tree<PsiElement> {
    override fun value(): PsiElement = element
    override fun isTree(): Boolean = element is YAMLMapping

    override fun findChild(name: String): Tree<PsiElement>? =
        (element as YAMLMapping)
            .getKeyValueByKey(name)
            ?.value
            ?.let(::YamlElementTree)

    override fun findChildren(prefix: String): List<Tree<PsiElement>> {
        return (element as YAMLMapping)
            .keyValues
            .filter { it.key!!.text.startsWith(prefix) }
            .map { YamlElementTree(it.key!!) }
    }
    companion object {
        /**
         * Creates YamlElementTree instance
         */
        fun create(file: PsiElement): YamlElementTree? {
            val fileRoot = PsiTreeUtil.getChildOfType(file, YAMLDocument::class.java)
            return (PsiTreeUtil.getChildOfType(fileRoot, YAMLMapping::class.java) ?: fileRoot)?.let {YamlElementTree(it)}
        }
    }
}