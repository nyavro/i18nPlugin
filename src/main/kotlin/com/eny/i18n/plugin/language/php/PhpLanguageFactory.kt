package com.eny.i18n.plugin.language.php

import com.eny.i18n.plugin.factory.FoldingProvider
import com.eny.i18n.plugin.factory.LanguageFactory
import com.eny.i18n.plugin.factory.TranslationExtractor
import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.default
import com.eny.i18n.plugin.utils.unQuote
import com.eny.i18n.plugin.utils.whenMatches
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.FunctionReference
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression

/**
 * Php language components factory
 */
class PhpLanguageFactory: LanguageFactory {
    override fun translationExtractor(): TranslationExtractor = PhpTranslationExtractor()
    override fun foldingProvider(): FoldingProvider = PhpFoldingProvider()
}

internal class PhpTranslationExtractor: TranslationExtractor {
    override fun canExtract(element: PsiElement): Boolean =
        (element.isPhpStringLiteral() || element.isBorderToken())
    override fun isExtracted(element: PsiElement): Boolean =
        PhpPatternsExt.phpArgument("t", 0).accepts(getTextElement(element.parent))
    override fun template(element: PsiElement): (argument: String) -> String = {"t($it)"}
    override fun text(element: PsiElement): String = getTextElement(element).text.unQuote()
    override fun textRange(element: PsiElement): TextRange = getTextElement(element).parent.textRange
    private fun getTextElement(element: PsiElement) =
        element.whenMatches {it.isBorderToken()}?.prevSibling.default(element)
    private fun PsiElement.isBorderToken(): Boolean = listOf("right double quote", "right single quote").contains(this.type())
    private fun PsiElement.isPhpStringLiteral(): Boolean = listOf("double quoted string", "single quoted string").contains(this.type())
}

internal class PhpFoldingProvider: FoldingProvider {
    override fun collectContainers(root: PsiElement): List<PsiElement> =
        PsiTreeUtil
            .findChildrenOfType(root, StringLiteralExpression::class.java)
            .filter { PhpPatternsExt.phpArgument("t", 0).accepts(it)}
    override fun collectLiterals(container: PsiElement): Pair<List<PsiElement>, Int> = Pair(listOf(container), 0)
    override fun getFoldingRange(container: PsiElement, offset: Int, psiElement: PsiElement): TextRange =
        PsiTreeUtil.getParentOfType(psiElement, FunctionReference::class.java).default(psiElement).textRange
}