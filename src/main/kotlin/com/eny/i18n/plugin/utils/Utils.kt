package com.eny.i18n.plugin.utils

/**
 * Chain function for applying action to not empty collection
 */
inline fun <T, C: Collection<T>, R> C.whenNotEmpty(block: (C) -> R): R? {
    return if (this.isNotEmpty()) block(this) else null
}

/**
 * Chain-style predicate matcher
 */
inline fun <C> C.whenMatches(predicate: (arg: C) -> Boolean): C? {
    return if (predicate(this)) this else null
}

/**
 * Applies transformation when predicate matches
 */
inline fun <C> C.whenMatchesDo(predicate: (arg: C) -> Boolean, block: (C) -> C): C = this.whenMatches(predicate)?.let(block) ?: this

/**
 * Converts nullable to Boolean
 */
fun <C> C?.toBoolean(): Boolean {
    return this != null
}

/**
 * Converts nullable to default
 */
fun <C> C?.default(value: C): C {
    return this ?: value
}

/**
 * Converts nullable to List
 */
fun <C> C?.nullableToList(): List<C> {
    return if (this == null) emptyList() else listOf(this)
}

/**
 * Converts nullable collection to collection
 */
fun <T> List<T>?.fromNullable(): List<T> {
    return if (this == null) emptyList() else this
}

/**
 * Splits list to head and tail
 */
fun <C> List<C>.headTail(): Pair<C?, List<C>?> = Pair(this.firstOrNull(), this.drop(1).whenMatches {it.isNotEmpty()})

/**
 * flips function arguments
 */
fun <C, D, E> ((c: C, d: D) -> E).flip(): ((d: D, c: C) -> E) = {d: D, c: C -> this(c, d)}

/**
 * Chained 'as?' operator
 */
inline fun <I, reified O : I> I.maybe(): O? {
    return this as? O;
}

/**
 * Safe get element by index or null
 */
fun <T> Array<T>.at(index: Int): T? =
    if (this.size > index) this[index] else null

fun <T, A> Collection<T>.foldWhileAccum(accum: A, block: (A, T) -> A?): A? {
    var acc: A? = accum
    for (item in this) {
        acc = block(acc!!, item)
        if(acc == null) break
    }
    return acc
}
