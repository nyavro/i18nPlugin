package com.eny.i18n.plugin.parser

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

class KeyParserDefinition : ParserDefinition {

    override fun createParser(project: Project?): PsiParser {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createFile(viewProvider: FileViewProvider?): PsiFile {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStringLiteralElements(): TokenSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFileNodeType(): IFileElementType {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createLexer(project: Project?): Lexer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createElement(node: ASTNode?): PsiElement {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommentTokens(): TokenSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}