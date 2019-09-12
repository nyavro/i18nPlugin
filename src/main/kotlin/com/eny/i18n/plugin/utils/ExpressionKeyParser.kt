package com.eny.i18n.plugin.utils

import com.intellij.vcs.log.history.removeTrivialMerges

data class FullKey(val fileName:List<Token>, val compositeKey:List<Token>)

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
        } else if (token.type() == TokenType.Asterisk) {
            TODO()
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
            return WaitingNsKsLiteral(maybeFile + token)
        } else if (token.type() == TokenType.Template) {
            return unwrapTemplateExpression(token as TemplateExpression)
        } else {
            return Error("Invalid ns separator position (0)")
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
        } else {
            return Error("Ivalid token " + token.text())
        }
    }
    override fun fullKey(): FullKey? = FullKey(listOf(), keys)
}

class FullKeyWaitingKey(val file: List<Token>, val key: List<Token>) : State {
    override fun next(token: Token): State {
        if (token == Asterisk) {
            return FullKeyWaitingKS(file, listOf(token))
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
        } else {
            return Error("Invalid token " + token.text())
        }
    }
    override fun fullKey(): FullKey? = FullKey(file, key)
}

class IncompleteNs(val tokens: List<Token>): State {
    override fun next(token: Token): State {
        if (tokens.isEmpty()) {
            if (token.type() == TokenType.NsSeparator) {
                return Error("Invalid ns separator position (0)")
            } else if (token.type() == TokenType.KeySeparator) {
                return Error("Invalid key separator position (0)")
            }
        }
        else {
        }
        return Error("")
//        if (token.resolved().isEmpty()) {
//            return IncompleteNs
//        }

    }

    override fun fullKey(): FullKey? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class ExpressionKeyParser {
    val FILENAME_SEPARATOR = ":"
    val COMPOSITE_KEY_SEPARATOR = "."

    fun parseOld(elements: List<KeyElement>): FullKey? {
//        return elements.fold(Start() as State) {
//            state, element -> state.next(element)
//        }.fullKey()
        return null
//        val splitFileName = splitFileName(Triple(listOf(), null, listOf()), elements)
//        if (splitFileName != null) {
//            val (fileNameElements, separatorElement, compositeKeyElements) = splitFileName
//            val (filePart, keyPart) = splitFileNameKey(separatorElement)
//            val fileName = fileNameElements + filePart
//            val compositeKey = parseCompositeKey(keyPart + compositeKeyElements)
//            if (fileName.isEmpty() && separatorElement == null && compositeKey.size == 1) {
//                return null
//            }
//            return FullKey(fileName, compositeKey)
//        }
//        else {
//            return null
//        }
    }

    fun parse(elements: List<KeyElement>): FullKey? {
        val tokens = Tokenizer(":", ".").tokenizeAll(elements)
        return tokens.fold(WaitingFileName() as State) {
            state, token -> state.next(token)
        }.fullKey()
    }

    private fun parseCompositeKey(elements: List<KeyElement>): List<KeyElement> = elements.flatMap { key -> splitKey(key)}

    private fun splitKey(element: KeyElement): List<KeyElement> {
        if (element.resolvedTo != null) {
            val split = element.resolvedTo.split(COMPOSITE_KEY_SEPARATOR)
            return if (element.type == KeyElementType.TEMPLATE) {
                split.mapIndexed {
                    index, item -> if (index == 0) KeyElement(element.text, item, KeyElementType.TEMPLATE)
                        else KeyElement("", item, KeyElementType.TEMPLATE)
                }
            }
            else {
                split.map {
                    item -> KeyElement(item, item, KeyElementType.LITERAL)
                }
            }
        }
        return listOf(element)
    }

    fun splitFileName(acc: Triple<List<KeyElement>, KeyElement?, List<KeyElement>>, elements: List<KeyElement>): Triple<List<KeyElement>, KeyElement?, List<KeyElement>>? {
        if (elements.isEmpty()) {
            val (filePart, separator, keyPart) = acc
            if (separator == null && keyPart.isEmpty()) {
                return Triple(listOf(), null, filePart)
            }
            return acc
        }
        else {
            val (filePart, separator, keyPart) = acc
            val head = elements.first()
            return if (head.resolvedTo != null && head.resolvedTo.contains(FILENAME_SEPARATOR)) {
                if (separator != null || head.resolvedTo.split(FILENAME_SEPARATOR).size != 2) {
                    null
                } else {
                    splitFileName(Triple(filePart, head, keyPart), elements.drop(1))
                }
            } else {
                if (separator != null) {
                    splitFileName(Triple(filePart, separator, keyPart + head), elements.drop(1))
                } else {
                    splitFileName(Triple(filePart + head, separator, keyPart), elements.drop(1))
                }
            }
        }
    }

    fun splitFileNameKey(element: KeyElement?): Pair<List<KeyElement>, List<KeyElement>> {
        return if(element == null || element.resolvedTo == null || element.resolvedTo == FILENAME_SEPARATOR) {
            Pair(listOf(), listOf())
        }
        else {
            val (filePart, keyPart) = element.resolvedTo.split(FILENAME_SEPARATOR)
            if (element.type == KeyElementType.LITERAL) {
                Pair(
                    if (filePart.isEmpty()) listOf() else listOf(KeyElement.fromLiteral(filePart)),
                    if (keyPart.isEmpty()) listOf() else listOf(KeyElement.fromLiteral(keyPart))
                )
            } else {
                Pair(
                    listOf(KeyElement(element.text, filePart, KeyElementType.TEMPLATE)),
                    listOf(KeyElement("", keyPart, KeyElementType.TEMPLATE))
                )
            }
        }
    }
}
