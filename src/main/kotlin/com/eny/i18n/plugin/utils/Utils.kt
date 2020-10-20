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
 * Modifies source with block when predicate matches
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
 * map with accumulator
 */
fun <T, A, R: Any> List<T>.mapAccum(accum: A, block: (T, A) -> Pair<R?, A>): List<R> {
    var acc = accum
    return this.mapNotNull {
        val (first, second) = block(it, acc)
        acc = second
        first
    }
}

/**
 * map with accumulator
 */
fun <T, A> List<T>.filterAccum(accum: A, predicate: (T, A) -> A?): List<T> {
    var acc = accum
    return this.filter { predicate(it, acc)?.also {acc = it}.toBoolean() }
}

fun <T, A> Collection<T>.takeWhileAccum(accum: A, predicate: (T, A) -> A?): List<T> {
    var acc: A = accum
    return this.takeWhile {item -> predicate(item, acc)?.also {acc = it}.toBoolean()}
}

inline fun <I, reified O : I> I.maybe(): O? {
    return this as? O;
}

