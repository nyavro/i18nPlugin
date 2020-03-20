package com.eny.i18n.plugin.ide

import com.intellij.lang.Language
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.InitialPatternCondition
import com.intellij.psi.InjectedLanguagePlaces
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.util.ProcessingContext
import com.jetbrains.php.injection.PhpElementPattern
import com.jetbrains.php.lang.psi.elements.FunctionReference
import com.jetbrains.php.lang.psi.elements.ParameterList
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression

class I18nMultiHostInjector : MultiHostInjector {
    val patterns = listOf(JSPatterns.jsArgument("t", 0), phpArgument("t", 0))

    fun getLanguagesToInject(host: PsiLanguageInjectionHost, injectionPlacesRegistrar: InjectedLanguagePlaces) {
        val text = host.text
        if (host.isValidHost && host.textRange.length > 1 && patterns.any {pattern -> pattern.accepts(host)}) {
            val range = TextRange(1, host.textRange.length - 1)
            injectionPlacesRegistrar.addPlace(Language.findLanguageByID("I18n")!!, range, null, null);
        }
    }

    private fun phpArgument(name: String, index: Int, argCount: Int = -1): PhpElementPattern.Capture<StringLiteralExpression> {
        return PhpElementPattern.Capture<StringLiteralExpression>(
            object : InitialPatternCondition<StringLiteralExpression>(StringLiteralExpression::class.java) {
                override fun accepts(o: Any?, context: ProcessingContext): Boolean {
                    if (o is StringLiteralExpression) {
                        val parameterList = o.parent
                        if (parameterList is ParameterList) {
                            val parameters = parameterList.parameters
                            val function = parameterList.parent
                            return (index < parameters.size) && (parameters[index] == o) && (function is FunctionReference) && (function.name == name)
                        }
                    }
                    return false
                }
            }
        )
    }

    override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
        return mutableListOf(PsiLanguageInjectionHost::class.java)
    }

    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        val text = context.text
        if (context !is PsiLanguageInjectionHost) return
//        const host =
//        if (host.isValidHost && host.textRange.length > 1 && patterns.any {pattern -> pattern.accepts(host)}) {
//            val range = TextRange(1, host.textRange.length - 1)
//            injectionPlacesRegistrar.addPlace(Language.findLanguageByID("I18n")!!, range, null, null);
//        }
//        context.language
        if (text.contains("{{") && text.contains("}}") && context.isValidHost) {
            registrar.startInjecting(Language.findLanguageByID("I18n")!!)
                .addPlace(null, null, context, TextRange(1, context.textRange.length - 1))
                .doneInjecting()
        }
        print(text)
    }
}