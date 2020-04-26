package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.language.php.PhpPatternsExt
import com.jetbrains.php.lang.psi.elements.FunctionReference
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression

/**
 * Php i18n folding builder
 */
class PhpFoldingBuilder: FoldingBuilderBase<FunctionReference>(
    FunctionReference::class.java,
    collectElementsOfType(StringLiteralExpression::class.java, PhpPatternsExt.phpArgument("t", 0))
)