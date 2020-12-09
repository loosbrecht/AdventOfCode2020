package main.kotlin.day9

import main.kotlin.util.readInputForDay
import java.util.*

fun main() {
    val inputList = readInputForDay(9).map { it.toLong() }
    val errorValue = findInvalidNumber(inputList)
    val sumList = findSumListForErrorValue(inputList, errorValue)
    val min = sumList.minOrNull()!!
    val max = sumList.maxOrNull()!!
    val weakness = min + max
    println(errorValue)
    println(weakness)
}

fun findSumListForErrorValue(inputList: List<Long>, errorValue: Long): List<Long> {
    val sumQueue: Queue<Long> = LinkedList<Long>()
    for (input in inputList) {
        var sum = sumQueue.sum()
        while (sum > errorValue) {
            sumQueue.remove()
            sum = sumQueue.sum()
        }
        if (sum == errorValue) {
            return sumQueue.toList()
        }

        sumQueue.add(input)
    }
    return emptyList()
}

fun findInvalidNumber(inputList: List<Long>, preambleSize: Int = 25): Long {
    val preamble: Queue<Long> = LinkedList<Long>()
    var errorValue = -1L
    for ((i, l) in inputList.withIndex()) {
        //initial fill of the preamble set
        if (i < preambleSize) {
            preamble.add(l)
            continue
        }
        if (!validateValue(preamble, l)) {
            errorValue = l
            break
        }
        preamble.remove()
        preamble.add(l)
    }
    return errorValue
}

fun validateValue(preamble: Queue<Long>, value: Long): Boolean {
    preamble.toList()
    for ((i, p1) in preamble.withIndex()) {
        for (p2 in preamble.drop(i + 1)) {
            if (value == p1 + p2) {
                return true
            }
        }
    }
    return false
}
