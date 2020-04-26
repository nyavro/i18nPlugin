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
                    override fun accepts(o: Any?, context: ProcessingContext): Boolean =
                        (o as? StringLiteralExpression)
                            ?.let { it.parent as? ParameterList }
                            ?.let {
                                val function = it.parent
                                (index < it.parameters.size) && (it.parameters[index] == o) &&
                                    (function is FunctionReference) && (function.name == functionName)
                            } ?: false
                }
            )
    }
}