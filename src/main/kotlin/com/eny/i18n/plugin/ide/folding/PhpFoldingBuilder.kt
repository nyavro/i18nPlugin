package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.language.php.PhpPatternsExt
import com.jetbrains.php.injection.PhpElementPattern
import com.jetbrains.php.lang.psi.elements.FunctionReference
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression

/**
 * Php i18n folding builder
 */
class PhpFoldingBuilder: FoldingBuilderBase<StringLiteralExpression, StringLiteralExpression, FunctionReference, PhpElementPattern.Capture<StringLiteralExpression>>(
    PhpPatternsExt.phpArgument("t", 0),
    StringLiteralExpression::class.java,
    FunctionReference::class.java
)