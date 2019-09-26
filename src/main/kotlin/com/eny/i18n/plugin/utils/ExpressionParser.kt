package com.eny.i18n.plugin.utils

import java.util.*

class ExpressionParser {
    private val dropItems = listOf("`", "{", "}", "$")
    fun parse(elements: List<KeyElement>): List<KeyElement> {
        return elements.mapNotNull {
            item -> when {
                dropItems.contains(item.text) -> null
                item.type == KeyElementType.TEMPLATE -> item.copy(text="\${${item.text}}")
                else -> item
            }
        }
    }
}