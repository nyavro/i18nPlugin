package com.eny.i18n.plugin.language.php

import com.intellij.patterns.InitialPatternCondition
import com.intellij.util.ProcessingContext
import com.jetbrains.php.injection.PhpElementPattern
import com.jetbrains.php.lang.psi.elements.FunctionReference
import com.jetbrains.php.lang.psi.elements.ParameterList
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression

/**
 * Php platform extended patterns
 */
class PhpPatternsExt {

    companion object {

        /**
         * Captures argument of php function call
         */
        fun phpArgument(functionName: String, index: Int): PhpElementPattern.Capture<StringLiteralExpression> =
            PhpElementPattern.Capture<StringLiteralExpression>(
                object : InitialPatternCondition<StringLiteralExpression>(StringLiteralExpression::class.java) {
                    override fun accepts(o: Any?, context: ProcessingContext): Boolean {
                        if (o is StringLiteralExpression) {
                            val parameterList = o.parent
                            if (parameterList is ParameterList) {
                                val parameters = parameterList.parameters
                                val function = parameterList.parent
                                return (index < parameters.size) && (parameters[index] == o) && (function is FunctionReference) && (function.name == functionName)
                            }
                        }
                        return false
                    }
                }
            )
    }
}