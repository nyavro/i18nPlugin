package utils

import kotlin.random.Random

private val random = Random(System.currentTimeMillis())

fun <T> randomOf(vararg items: T): T = items.get(random.nextInt(items.size))