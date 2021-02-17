package com.eny.i18n.plugin.utils

import java.io.File

/**
 * Calculate path (relative) to given root
 */
fun pathToRoot(rootPath: String, path: String): String =
    path
        .whenMatches { it.startsWith(rootPath) }
        ?.substring(rootPath.length)
        .default(path)

/**
 * Distance between two elements in a tree.
 * Counted as number of steps to do to go from one element to another
 */
fun distance(pathA: String, pathB: String): Int {
    val partsA = if (pathA=="") emptyList() else pathA.split(File.separator)
    val partsB = if (pathB=="") emptyList() else pathB.split(File.separator)
    fun dist(a: List<String>, b: List<String>): Int =
        if (a.isNotEmpty() && b.isNotEmpty() && a.first() == b.first())
            dist(a.drop(1), b.drop(1))
        else a.size + b.size
    return dist(partsA, partsB)
}