package com.eny.i18n.plugin.addons.technology.php

import com.intellij.patterns.InitialPatternCondition
import com.intellij.util.ProcessingContext
import com.jetbrains.php.injection.PhpElementPattern
import com.jetbrains.php.lang.psi.elements.FunctionReference
import com.jetbrains.php.lang.psi.elements.ParameterList
import com.jetbrains.php.lang.psi.elements.PhpExpression

/**
 * Php platform extended patterns
 */
class PhpPatternsExt {

    companion object {

        /**
         * Captures argument of php function call
         */
        fun phpArgument(functionName: String, index: Int): PhpElementPattern.Capture<PhpExpression> =
            PhpElementPattern.Capture<PhpExpression>(
                object : InitialPatternCondition<PhpExpression>(PhpExpression::class.java) {
                    override fun accepts(o: Any?, context: ProcessingContext): Boolean =
                        (o as? PhpExpression)
                            ?.let { it.parent as? ParameterList }
                            ?.let {
                                val function = it.parent
                                (index < it.parameters.size) && (it.parameters[index] == o) &&
                                    (function is FunctionReference) && (function.name == functionName)
                            } ?: false
                }
            )

        /**
         * Captures argument of php function call
         */
        fun phpArgument(): PhpElementPattern.Capture<PhpExpression> =
            PhpElementPattern.Capture<PhpExpression>(
                object : InitialPatternCondition<PhpExpression>(PhpExpression::class.java) {
                    override fun accepts(o: Any?, context: ProcessingContext): Boolean =
                        (o as? PhpExpression)
                            ?.let { it.parent as? ParameterList }
                            ?.let {
                                val function = it.parent
                                (0 < it.parameters.size) && (it.parameters[0] == o) &&
                                    (function is FunctionReference)
                            } ?: false
                }
            )
    }
}