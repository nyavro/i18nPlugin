package com.eny.i18n.plugin.ide.folding

import com.jetbrains.php.lang.psi.elements.FunctionReference

/**
 * Php i18n folding builder
 */
class PhpFoldingBuilder: FoldingBuilderBase<FunctionReference>(
    FunctionReference::class.java,
    FoldingElementsCollectorPhp()
)