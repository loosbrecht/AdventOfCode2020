package main.kotlin.day5

import java.io.File
import kotlin.math.ceil
import kotlin.math.floor

fun main() {
    val idList = readFileToLines("input/day5.txt").map { findSeating(it) }.sorted()
    val maxId = idList.maxOrNull()
    val missingId = findMissingId(idList)
    println("Max seatID $maxId")
    println("Missing seatID $missingId")
}

fun readFileToLines(fileName: String): List<String> {
    return File(fileName).readLines()
}

fun findMissingId(idList: List<Int>): Int {
    var id = idList[0]
    for (i in idList) {
        if (id != i) {
            return id
        }
        id++
    }
    return id
}

fun findSeating(line: String): Int {
    var upper = 127.0
    var lower = 0.0
    var left = 0.0
    var right = 7.0

    val upFunc: (l: Double, u: Double) -> Double = { l, u -> floor(l + (u - l) / 2) }
    val lowFunc: (l: Double, u: Double) -> Double = { l, u -> ceil(l + (u - l) / 2) }
    for (c in line) {
        when (c) {
            'F' -> upper = upFunc(lower, upper)
            'B' -> lower = lowFunc(lower, upper)
            'L' -> right = upFunc(left, right)
            'R' -> left = lowFunc(left, right)
        }
    }
    val row = lower.toInt()
    val seat = left.toInt()
    val seatId = row * 8 + seat
    //println("$row $seat $seatId ")
    return seatId
}

