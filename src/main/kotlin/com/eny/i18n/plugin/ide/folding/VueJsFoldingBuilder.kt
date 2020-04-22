package com.eny.i18n.plugin.ide.folding

import com.intellij.lang.javascript.psi.JSCallExpression

/**
 * Vue Js i18n folding builder
 */
class VueJsFoldingBuilder: FoldingBuilderBase<JSCallExpression>(
    JSCallExpression::class.java,
    FoldingElementsCollectorVue()
)
