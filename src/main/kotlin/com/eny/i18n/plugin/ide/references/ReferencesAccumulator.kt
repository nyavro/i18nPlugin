package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * Accumulates references
 */
class ReferencesAccumulator(private val key: String) {

    private val res = mutableListOf<PsiElement>()

    /**
     * Processing function for PsiSearchHelper
     */
    fun process() = {
        entry: PsiElement, offset:Int ->
            val typeName = entry.node.elementType.toString()
            if (entry.text.unQuote().startsWith(key)) {
                if (listOf("JS:STRING_LITERAL", "quoted string").any { item -> typeName.contains(item) }) {
                    res.add(entry)
                } else if (typeName == "JS:STRING_TEMPLATE_PART") {
                    res.add(entry.parent)
                } else if (typeName == "JS:STRING_TEMPLATE_EXPRESSION") {
                    res.add(entry)
                }
            }
            true
    }

    /**
     * Returns collected entries
     */
    fun entries(): Collection<PsiElement> = res
}