package main.kotlin.day1

import java.io.File

fun main() {

    val inputValues = readFileLineByLineUsingForEachLine("input/day1.txt")
    println(solvePart1(inputValues))
    println(solvePart2(inputValues))


}

fun solvePart1(inputValues: List<Int>): Int {
    for ((i, firstValue) in inputValues.withIndex()) {
        for (secondValue in inputValues.subList(i + 1, inputValues.size)) {
            if (firstValue + secondValue == 2020) {
                return firstValue * secondValue
            }
        }
    }
    return 0
}

fun solvePart2(inputValues: List<Int>): Int {
    for ((i, firstValue) in inputValues.withIndex()) {
        for ((j, secondValue) in inputValues.subList(i + 1, inputValues.size).withIndex()) {
            for (thirdValue in inputValues.subList(j + 1, inputValues.size)) {
                if (firstValue + secondValue + thirdValue == 2020) {
                    return firstValue * secondValue * thirdValue
                }
            }
        }
    }
    return 0
}

fun readFileLineByLineUsingForEachLine(fileName: String): List<Int> {
    return File(fileName).readLines().map { it.toInt() }.sorted()
}