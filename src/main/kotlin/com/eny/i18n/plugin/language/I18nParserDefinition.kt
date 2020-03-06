package com.eny.i18n.plugin.language

import com.eny.i18n.plugin.language.parser.I18nParser
import com.eny.i18n.plugin.language.psi.I18nFile
import com.eny.i18n.plugin.language.psi.I18nTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class I18nParserDefinition : ParserDefinition {

    companion object {
        val File = IFileElementType(I18nLanguage.Instance)
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile = I18nFile(viewProvider)

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun getCommentTokens(): TokenSet = TokenSet.EMPTY

    override fun getFileNodeType(): IFileElementType = File

    override fun createParser(project: Project?): PsiParser = I18nParser()

    override fun createLexer(project: Project): Lexer = I18nLexerAdapter()

    override fun createElement(node: ASTNode?): PsiElement = I18nTypes.Factory.createElement(node)
}