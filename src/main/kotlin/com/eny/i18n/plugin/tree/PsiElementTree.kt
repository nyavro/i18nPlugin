package com.eny.i18n.plugin.tree

import com.eny.i18n.plugin.utils.unQuote
import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

abstract class PsiElementTree: Tree<PsiElement> {
    companion object {
        fun create(file: PsiElement): PsiElementTree? =
            if (file is JsonFile) JsonElementTree.create(file)
            else YamlElementTree.create(file)
    }
}
class JsonElementTree(val element: PsiElement): PsiElementTree() {
    override fun value(): PsiElement = element
    override fun isTree(): Boolean = element is JsonObject
    override fun findChild(name: String): Tree<PsiElement>? =
        if (element is JsonObject) element.findProperty(name)?.value?.let { child -> JsonElementTree(child) }
        else null
    override fun findChildren(regex: Regex): List<Tree<PsiElement>> =
        element
            .node
            .getChildren(TokenSet.create(JsonElementTypes.PROPERTY))
            .asList()
            .map {item -> item.firstChildNode.psi}
            .filter {item -> item != null && item.text.unQuote().matches(regex)}
            .map {item -> JsonElementTree(item)}
    companion object {
        fun create(file: PsiElement): JsonElementTree? =
            PsiTreeUtil.getChildOfType(file, JsonObject::class.java)?.let{ fileRoot -> JsonElementTree(fileRoot)}
    }
}
class YamlElementTree(val element: PsiElement): PsiElementTree() {
    override fun value(): PsiElement = element
    override fun isTree(): Boolean = element is YAMLMapping
    override fun findChild(name: String): Tree<PsiElement>? =
        if (element is YAMLMapping) element.getKeyValueByKey(name)?.value?.let { child -> YamlElementTree(child) }
        else null

    override fun findChildren(regex: Regex): List<Tree<PsiElement>> {
        return (element as YAMLMapping)
            .keyValues
            .filter {
                keyValue -> keyValue.key?.text?.matches(regex) ?: false
            }
            .mapNotNull {item -> item.key?.let { key -> YamlElementTree(key)}}
    }
    companion object {
        fun create(file: PsiElement): YamlElementTree? {
            val fileRoot = PsiTreeUtil.getChildOfType(file, YAMLDocument::class.java)
            return PsiTreeUtil.getChildOfType(fileRoot, YAMLMapping::class.java)?.let{ root -> YamlElementTree(root)}
        }
    }
}

abstract class PsiProperty: FlippedTree<PsiElement> {
    companion object {
        fun create(element: PsiElement): PsiProperty =
            if (element.containingFile is JsonFile) JsonProperty(element)
            else YamlProperty(element)
    }
}
class JsonRoot(val element: PsiFile): PsiProperty() {
    override fun name() = element.containingFile.name.substringBeforeLast(".")
    override fun isRoot() = true
    override fun ancestors(): List<FlippedTree<PsiElement>> = listOf()
}
class JsonProperty(val element: PsiElement): PsiProperty() {
    override fun name() = element.firstChild.text.unQuote()
    override fun isRoot() = false
    override fun ancestors(): List<FlippedTree<PsiElement>> = allAncestors(element)
    protected fun allAncestors(item: PsiElement): List<FlippedTree<PsiElement>> {
        if (item is PsiFile) return listOf(JsonRoot(item))
        else if(item is JsonProperty) return allAncestors(item.parent) + JsonProperty(item)
        else return allAncestors(item.parent)
    }
}

class YamlRoot(val element: PsiFile): PsiProperty() {
    override fun name() = element.containingFile.name.substringBeforeLast(".")
    override fun isRoot() = true
    override fun ancestors(): List<FlippedTree<PsiElement>> = listOf()
}
class YamlProperty(val element: PsiElement): PsiProperty() {
    override fun name() = (element as YAMLKeyValue).key?.text?.unQuote() ?: ""
    override fun isRoot() = false
    override fun ancestors(): List<FlippedTree<PsiElement>> = allAncestors(element)
    protected fun allAncestors(item: PsiElement): List<FlippedTree<PsiElement>> {
        return (PsiTreeUtil
            .collectParents(item, YAMLKeyValue::class.java, true, {c -> c is YAMLDocument})
            .map {kv -> YamlProperty(kv)} +
            YamlRoot(item.containingFile)).reversed()
    }
}