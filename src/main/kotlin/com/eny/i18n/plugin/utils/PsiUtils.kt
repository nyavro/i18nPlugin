package com.eny.i18n.plugin.utils

import com.intellij.psi.PsiElement

/**
 * Gets element's type string
 */
fun PsiElement.type(): String = this.node?.elementType.toString()