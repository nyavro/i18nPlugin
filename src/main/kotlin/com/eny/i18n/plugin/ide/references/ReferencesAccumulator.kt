package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * Accumulates references
 */
class ReferencesAccumulator(private val key: String) {

    private val res = mutableListOf<PsiElement>()
    private var toDrop: PsiElement? = null

    /**
     * Processing function for PsiSearchHelper
     */
    fun process() = {
        entry: PsiElement, offset:Int ->
            val typeName = entry.node.elementType.toString()
            if (entry.text.unQuote().startsWith(key)) {
                if (listOf("JS:STRING_LITERAL", "quoted string").any { item -> typeName.contains(item) }) {
                    process(entry)
                } else if (typeName == "JS:STRING_TEMPLATE_PART") {
                    process(entry.parent)
                } else if (typeName == "JS:STRING_TEMPLATE_EXPRESSION") {
                    process(entry)
                }
            }
            true
    }

    /**
     * Returns collected entries
     */
    fun entries(): Collection<PsiElement> = res

    private fun getParentsOfType(element: PsiElement, types: Set<String>) {
        var cur = element.parent
        while(!(cur is PsiFile)) {
            val typeName = cur.node.elementType.toString()
            if (types.contains(typeName)) {
                toDrop = cur
            }
            cur = cur.parent
        }
    }

    private fun process(entry: PsiElement) {
        if (entry != toDrop) {
            getParentsOfType(entry, setOf("single quoted string", "double quoted string", "JS:STRING_LITERAL", "JS:STRING_TEMPLATE_PART", "JS:STRING_TEMPLATE_EXPRESSION"))
            res.add(entry)
        } else {
            toDrop = null
        }
    }
}