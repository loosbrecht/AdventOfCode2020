package main.kotlin.day10

import main.kotlin.util.readInputForDay
import main.kotlin.util.readOtherInputForDay
import main.kotlin.util.readSmallInputForDay

fun main() {

    val joltList = readInputForDay(10).map { it.toInt() }
    val result = findChainUsingEverything(joltList)
    println(result)

}



fun findChainUsingEverything(joltList: List<Int>): Int {
    var diffOne = 0
    var diffThree = 0
    var previous = 0
    val adapter = joltList.maxOrNull()!! + 3

    for (v in joltList.sorted()) {
        when (v - previous) {
            1 -> diffOne++
            3 -> diffThree++
        }
        previous = v
    }
    when (adapter - previous) {
        1 -> diffOne++
        3 -> diffThree++
    }
    println("$diffOne $diffThree")
    return diffOne * diffThree
}

