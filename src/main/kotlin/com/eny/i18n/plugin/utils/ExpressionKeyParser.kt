package com.eny.i18n.plugin.utils

interface State {
    fun next(token: Token): State
    fun fullKey(isTemplate: Boolean): FullKey? = null
}

class Error(val msg: String): State {
    override fun next(token: Token): State = this
    override fun toString(): String = "Error <$msg>"
}

class Start(val init: Literal?) : State {
    override fun next(token: Token): State =
        when {
            token is NsSeparator && init != null  -> WaitingLiteral(init, listOf())
            token is KeySeparator && init != null -> WaitingLiteral(null, listOf(init))
            token is Literal  -> Start(if (init != null) init.merge(token) else token)
            else -> Error("Invalid ns separator position (0)") // Never get here
        }
}

class WaitingLiteral(val file: Literal?, val key: List<Literal>) : State {
    override fun next(token: Token): State =
        when {
            token is Literal ->  WaitingLiteralOrSeparator(file, key + token)
            else -> Error("Invalid token " + token)
        }
}

class WaitingLiteralOrSeparator(val file: Literal?, val key: List<Literal>) : State {
    override fun next(token: Token): State =
        when {
            token is KeySeparator -> WaitingLiteral(file, key)
            token is Literal -> {
                val last = key.last().merge(token)
                val init = key.dropLast(1)
                WaitingLiteralOrSeparator(file, init + last)
            }
            else -> Error("Invalid token " + token)
        }
    override fun fullKey(isTemplate: Boolean): FullKey? = FullKey(file, key, isTemplate)
}

class ExpressionKeyParser {
    private val dropItems = listOf("`", "{", "}", "$")
    fun reduce(elements: List<KeyElement>): List<KeyElement> {
        return elements.mapNotNull {
            item -> when {
                dropItems.contains(item.text) -> null
                item.type == KeyElementType.TEMPLATE -> item.copy(text="\${${item.text}}")
                else -> item
            }
        }
    }

    fun parse(elements: List<KeyElement>, isTemplate: Boolean = false): FullKey? =
        Tokenizer(":", ".")
            .tokenizeAll(elements)
            .fold(Start(null) as State) {
                state, token -> state.next(token)
            }
            .fullKey(isTemplate)

    fun parseLiteral(text: String): FullKey? = parse(listOf(KeyElement.literal(text)))
}
