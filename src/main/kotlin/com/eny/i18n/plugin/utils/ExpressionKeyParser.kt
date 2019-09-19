package com.eny.i18n.plugin.utils

data class FullKey(val fileName:List<Token>, val compositeKey:List<Token>, val nsCorr: Boolean = false) {
    val nsLength = ln(fileName, false, true)
        get() = field
    val keyLength = ln(compositeKey, true, false)
        get() = field
    val length = nsLength + keyLength + (if (nsLength > 0) 1 else 0) + (if (nsCorr) 1 else 0)
        get() = field
    private fun ln(tokens: List<Token>, correctDots: Boolean, correctNs: Boolean):Int {
        val lengths = tokens.map { n -> n.textLength()}.filter { v -> v > 0}
        val dotCorrection = if (correctDots) tokens.map { n -> n.dotCorrection()}.sum() else 0
        return lengths.sum() + (if (lengths.isEmpty()) 0 else (lengths.size - 1)) - dotCorrection
    }
}

interface State {
    fun next(token: Token): State
    fun fullKey(): FullKey? {
        return null
    }
    fun unwrapTemplateExpression(token: TemplateExpression): State = token.resolved().fold(this) {
        state, item -> state.next(item)
    }
}

class Error(val msg: String): State {
    override fun next(token: Token): State = this
    override fun fullKey(): FullKey? = null
    override fun toString(): String = "Error <$msg>"
}

class WaitingFileName: State {
    override fun next(token: Token): State {
        if (token.type() == TokenType.NsSeparator) {
            return Error("Invalid ns separator position (0)")
        } else if (token.type() == TokenType.KeySeparator) {
            return Error("Invalid key separator position (0)")
        } else if (token.type() == TokenType.Template) {
            return token.resolved().fold(this as State) {
                state, item -> state.next(item)
            }
        } else {
            return WaitingNsKsLiteral(listOf(token))
        }
    }
}

class WaitingNsKsLiteral(val maybeFile: List<Token>) : State {
    override fun next(token: Token): State {
        if (token.type() == TokenType.NsSeparator) {
            return FullKeyWaitingKey(maybeFile, listOf())
        } else if (token.type() == TokenType.KeySeparator) {
            return DefaultWaitingLiteral(maybeFile)
        } else if (token.type() == TokenType.Literal) {
            val last = maybeFile.last().merge(token)
            val init = maybeFile.dropLast(1)
            return WaitingNsKsLiteral(init + last)
        } else if (token.type() == TokenType.Template) {
            return unwrapTemplateExpression(token as TemplateExpression)
        } else if (token.type() == TokenType.Asterisk) {
            val last = maybeFile.last().merge(token)
            val init = maybeFile.dropLast(1)
            return WaitingNsKsLiteral(init + last)
        } else {
            return Error("Invalid ns separator position (0)") // Never get here
        }
    }
}

class DefaultWaitingLiteral(val keys: List<Token>) : State {
    override fun next(token: Token): State {
        if (token.type() == TokenType.Literal) {
            return DefaultWaitingKs(keys + token)
        } else {
            return Error("Invalid token " + token.text())
        }
    }
}

class DefaultWaitingKs(val keys: List<Token>) : State {
    override fun next(token: Token): State {
        if (token is KeySeparator) {
            return DefaultWaitingLiteral(keys)
        } else if(token.type() == TokenType.Literal) {
            val last = keys.last().merge(token)
            val init = keys.dropLast(1)
            return DefaultWaitingKs(init + last)
        } else {
            return Error("Ivalid token " + token.text())
        }
    }
    override fun fullKey(): FullKey? = FullKey(listOf(), keys, false)
}

class FullKeyWaitingKey(val file: List<Token>, val key: List<Token>) : State {
    override fun next(token: Token): State {
        if (token.type() == TokenType.Asterisk) {
            return FullKeyWaitingKS(file, key + token)
        } else if (token.type() == TokenType.Literal) {
            return FullKeyWaitingKS(file, key + token)
        } else if (token.type() == TokenType.Template) {
            return unwrapTemplateExpression(token as TemplateExpression)
        } else {
            return Error("Invalid token " + token.text())
        }
    }
}

class FullKeyWaitingKS(val file: List<Token>, val key: List<Token>) : State {
    override fun next(token: Token): State {
        if(token.type() == TokenType.KeySeparator) {
            return FullKeyWaitingKey(file, key)
        }
        if(token.type() == TokenType.Literal) {
            val last = key.last().merge(token)
            val init = key.dropLast(1)
            return FullKeyWaitingKS(file, init + last)
        }
        else if (token.type() == TokenType.Asterisk) {
            val last = key.last().merge(token)
            val init = key.dropLast(1)
            return FullKeyWaitingKS(file, init + last)
            //return FullKeyWaitingKS(file, key + token)
        }
        if(token.type() == TokenType.Template) {
            return unwrapTemplateExpression(token as TemplateExpression)
        }
        else {
            return Error("Invalid token " + token.text())
        }
    }
    override fun fullKey(): FullKey? = FullKey(file, key, false)
}

class ExpressionKeyParser {
    val FILENAME_SEPARATOR = ":"
    val COMPOSITE_KEY_SEPARATOR = "."

    fun parse(elements: List<KeyElement>): FullKey? {
        val tokens = Tokenizer(":", ".").tokenizeAll(elements)
        return tokens.fold(WaitingFileName() as State) {
            state, token -> state.next(token)
        }.fullKey()
    }
}
