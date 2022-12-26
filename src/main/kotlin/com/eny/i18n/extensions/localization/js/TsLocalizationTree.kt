package com.eny.i18n.extensions.localization.js

import com.eny.i18n.plugin.tree.Tree
import com.intellij.psi.PsiElement

class TsLocalizationTree(val element: PsiElement): Tree<PsiElement> {
    override fun findChild(name: String): Tree<PsiElement>? {
        TODO("Not yet implemented")
    }

    override fun isTree(): Boolean {
        TODO("Not yet implemented")
    }

    override fun value(): PsiElement {
        TODO("Not yet implemented")
    }

    override fun findChildren(prefix: String): List<Tree<PsiElement>> {
        TODO("Not yet implemented")
    }


}
