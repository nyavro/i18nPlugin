package com.eny.i18n.plugin.ide.folding

import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression

/**
 * Js i18n folding builder
 */
class JsFoldingBuilder: FoldingBuilderBase<JSCallExpression>(
    JSCallExpression::class.java,
    collectElementsOfType(JSLiteralExpression::class.java, JSPatterns.jsArgument("t", 0))
)