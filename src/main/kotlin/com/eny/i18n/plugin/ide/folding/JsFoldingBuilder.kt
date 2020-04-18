package com.eny.i18n.plugin.ide.folding

import com.intellij.lang.javascript.patterns.JSElementPattern
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression

/**
 * Js i18n folding builder
 */
class JsFoldingBuilder: FoldingBuilderBase<JSExpression, JSLiteralExpression, JSCallExpression, JSElementPattern.Capture<JSExpression>>(
    JSPatterns.jsArgument("t", 0),
    JSLiteralExpression::class.java,
    JSCallExpression::class.java
)