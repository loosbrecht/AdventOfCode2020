package main.kotlin.day2

import main.kotlin.util.readInputForDay
import java.io.File

fun main() {
    val allLines = readInputForDay(2).map { splitLineInInput(it) }
    val solutionPart1 = allLines.filter { it.validatePar1() }.count()
    val solutionPart2 = allLines.filter { it.validatePart2() }.count()
    println(solutionPart1)
    println(solutionPart2)
}

class Input(private val lower: Int, private val higher: Int, private val key: Char, private val password: String) {
    fun validatePar1(): Boolean {
        val count = password.filter { c -> key == c }.count()
        return count in lower..higher
    }

    fun validatePart2(): Boolean {
        return (password[lower - 1] == key).xor(password[higher - 1] == key)
    }
}

fun splitLineInInput(line: String): Input {
    val split = line.split(" ")
    val range = split[0].split("-")
    val key = split[1][0]
    return Input(range[0].toInt(), range[1].toInt(), key, split[2])
}