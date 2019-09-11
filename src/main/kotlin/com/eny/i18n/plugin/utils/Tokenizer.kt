package com.eny.i18n.plugin.utils

class Tokenizer {

    private fun <T>join(list: List<T>, separator: T): List<T> {
        return if (list.size > 1) list.drop(1).fold(listOf(list[0])) { s, i -> s + separator + i}
        else list
    }

    private fun <T>join2(list: List<List<T>>, separator: T): List<T> {
        return if (list.size > 1) list.drop(1).fold(list[0]) { s, i -> s + separator + i}
        else list.flatten()
    }

    fun tokenize(element: KeyElement): List<Token> {
        if (element.type == KeyElementType.LITERAL) {
            return join2(
                    element.text.split(":").toList().map {
                item -> join(item.split(".").toList().map {value -> Literal(value)}, KeySeparator("."))
            }, FileNameSeparator(":"))
        } else {
            if (element.resolvedTo != null) {
                return join2(
                        element.text.split(":").toList().map {
                            item -> join(item.split(".").toList().map {value -> Literal(value)}, KeySeparator("."))
                        }, FileNameSeparator(":"))
            }
            else {
                return listOf(TemplateExpression(element.text, element.resolvedTo))
            }
        }
    }
}