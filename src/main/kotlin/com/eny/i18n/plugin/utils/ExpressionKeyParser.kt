package com.eny.i18n.plugin.utils

data class FullKey(val fileName:List<KeyElement>, val compositeKey:List<KeyElement>)

interface State {
    fun next(element: KeyElement): State
    fun fullKey(): FullKey?
}

class Start : State {
    override fun fullKey(): FullKey? {
        return null
    }

    override fun next(element: KeyElement): State {
        if (element.resolvedTo != null) {
            val split = element.resolvedTo.split(":")
            if (split.size == 1) {
                return IncompleteFileName(listOf(element))
            } else if (split.size > 2) {
                return Error("Multiple file name separators")
            } else {
                val fileName = if (element.type == KeyElementType.LITERAL) {
                    KeyElement.fromLiteral(split[0])
                } else {
                    KeyElement(element.text, split[0], KeyElementType.TEMPLATE)
                }
                val state = IncompleteKey(fileName, listOf())
                val key = if (element.type == KeyElementType.LITERAL) {
                    KeyElement(split[1], split[1], KeyElementType.LITERAL)
                } else {
                    KeyElement("", split[1], KeyElementType.TEMPLATE)
                }
                return state.next(key)
            }
        } else {
            return Error("Unknown")
        }
    }
}

class IncompleteKey(val fileName: KeyElement, val compositeKey: List<KeyElement>) : State {
    override fun next(element: KeyElement): State {
        if (element.resolvedTo != null) {
            if (element.resolvedTo.isNotEmpty()) {
                if (element.resolvedTo.contains(":")) {
                    return Error("Multiple file divisors")
                }
                val split = element.resolvedTo.split(".")
                if (element.type == KeyElementType.LITERAL) {
                    if (split.size == 1) {
                        return IncompleteKey(fileName, compositeKey + KeyElement(split[0], split[0], KeyElementType.LITERAL))
                    }
                    return split.fold(this as State) { state, item ->
                        state.next(KeyElement(item, item, KeyElementType.LITERAL))
                    }
                } else {
                    if (split.size == 1) {
                        return IncompleteKey(fileName, compositeKey + KeyElement(split[0], split[0], KeyElementType.TEMPLATE))
                    }
                    return split.foldIndexed(this as State) { index, state, item ->
                        if (index == 0) state.next(KeyElement(element.text, item, KeyElementType.TEMPLATE))
                        else state.next(KeyElement("", item, KeyElementType.TEMPLATE))
                    }
                }
            } else {
                return Error("Empty composite key element")
            }
        } else {
            return IncompleteKey(fileName, compositeKey + element)
        }
    }

    override fun fullKey(): FullKey? {
        return FullKey(listOf(fileName), compositeKey)
    }

}

class DefaultFileIncompleteKey(val tokens: List<KeyElement>): State {
    override fun next(element: KeyElement): State {
        if (element.resolvedTo != null) {
            if (element.resolvedTo.isEmpty()) {
                return Error("Empty key element")
            } else {
                val split = element.resolvedTo.split(".")
                if (split.size == 1) {
                    return DefaultFileIncompleteKey(tokens + element)
                } else {
                    if (element.type == KeyElementType.LITERAL) {
                        return split.fold(this as State) {
                            state, item -> state.next(KeyElement(item, item, KeyElementType.LITERAL))
                        }
                    } else {
                        return split.foldIndexed(this as State) {
                            index, state, item -> state.next(KeyElement(if (index==0) element.text else "", item, KeyElementType.TEMPLATE))
                        }
                    }
                }
            }
        } else {
            return DefaultFileIncompleteKey(tokens + element)
        }
    }

    override fun fullKey(): FullKey? {
        return if (tokens.size == 1) null else FullKey(listOf(), tokens)
    }
}

class IncompleteFileName(val tokens: List<KeyElement>): State {
    override fun fullKey(): FullKey? {
        if(tokens.size == 1) {
            val (token) = tokens
            return DefaultFileIncompleteKey(listOf()).next(token).fullKey()
        }
        else return FullKey(listOf(), tokens)
    }
    override fun next(element: KeyElement): State {
        if (element.resolvedTo != null) {
            if (element.resolvedTo.isEmpty()) {
                return this
            }
            val split = element.resolvedTo.split(":")
            if (split.size == 1) {
                return IncompleteFileName(tokens + element)
            } else if (split.size > 2) {
                return Error("Multiple file name separators")
            } else {
                if (element.type == KeyElementType.LITERAL) {
                    val f = split[0]
                    val k = split[1]
                    this.next(KeyElement(f, f, KeyElementType.LITERAL))
                    return split.fold(this as State){
                        state, item -> state.next(KeyElement(item, item, KeyElementType.LITERAL))
                    }
                } else {
                    return split.foldIndexed(this as State){
                        index, state, item -> state.next(KeyElement(if(index == 0) element.text else "", item, KeyElementType.TEMPLATE))
                    }
                }
            }
        } else {
            return Error("Unknown")
        }
    }
}

class Error(reason: String): State {
    override fun fullKey(): FullKey? = null
    override fun next(element: KeyElement): State = this
}

class ExpressionKeyParser {
    val FILENAME_SEPARATOR = ":"
    val COMPOSITE_KEY_SEPARATOR = "."

    fun parse(elements: List<KeyElement>): FullKey? {
        return elements.fold(Start() as State) {
            state, element -> state.next(element)
        }.fullKey()
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
