package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.ExpressionKeyParser
import com.eny.i18n.plugin.utils.JsonSearchUtil
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class CompositeKeyCompletionContributor: CompletionContributor(), CompositeKeyResolver<PsiElement> {

    private val CompositeKeySeparator = "."
    private val NsSeparator = ":"

    private val parser = ExpressionKeyParser()

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, result)
        val text = parameters.position.text.unQuote().substringBefore(CompletionInitializationContext.DUMMY_IDENTIFIER)
        val endsWithKeySeparator = text.endsWith(CompositeKeySeparator)
        val endsWithFileNameSeparator = text.endsWith(NsSeparator)
        val fixed = if (endsWithFileNameSeparator || endsWithKeySeparator) {
            text.substring(0, text.length - 1)
        } else text
        val fullKey = parser.parseLiteral(fixed)
        if (fullKey?.ns != null) {
            val files = JsonSearchUtil(parameters.position.project).findFilesByName(fullKey.ns.text)
            files.forEach {
                file -> listCompositeKeyVariants(fullKey.compositeKey, PsiTreeUtil.getChildOfType(file, JsonObject::class.java)?.let{fileRoot -> PsiElementTree(fileRoot)}, !endsWithKeySeparator).forEach {
                    key -> result.addElement(LookupElementBuilder.create(text + key))
                }
            }
        }
    }
}