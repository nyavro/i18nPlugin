package com.eny.i18n.extensions.lang.php

import com.intellij.openapi.util.Key
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.InitialPatternCondition
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiFile
import com.intellij.util.ProcessingContext

class PsiUserDataPatterns {

    companion object {
        fun <T> withUserData(key: Key<T>, value: T): ElementPattern<PsiFile> {
            return object : PsiElementPattern.Capture<PsiFile>(object : InitialPatternCondition<PsiFile>(PsiFile::class.java) {
                override fun accepts(element: Any?, context: ProcessingContext?): Boolean {
                    return (element as? PsiFile)?.getUserData(key) == value
                }
            }){}
        }
    }
}
