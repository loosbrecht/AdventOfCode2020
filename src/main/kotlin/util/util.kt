package main.kotlin.util

import java.io.File

fun readInputForDay(day: Int): List<String> {
    return readLines("input/day$day/day$day.txt")
}

fun readSmallInputForDay(day: Int): List<String> {
    return readLines("input/day$day/day$day-small.txt")
}

fun readOtherInputForDay(day: Int, key: String): List<String> {
    return readLines("input/day$day/day$day-$key.txt")
}

private fun readLines(fileName: String): List<String> {
    return File(fileName).readLines()
}

fun <T> List<T>.safeSubList(fromIndex: Int, toIndex: Int): List<T> =
    this.subList(fromIndex.coerceAtLeast(0), toIndex.coerceAtMost(this.size))

fun String.safeSubSequence(fromIndex: Int, toIndex: Int): CharSequence =
    this.subSequence(fromIndex.coerceAtLeast(0), toIndex.coerceAtMost(this.length))


fun main() {
    for (day in 14..25) {
        val f = File("src/main/kotlin/day$day")
        f.mkdir()
        val f2 = File("src/main/kotlin/day$day/main.kt")
        f2.createNewFile()

    }
}