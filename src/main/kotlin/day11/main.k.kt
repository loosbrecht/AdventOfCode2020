package main.kotlin.day11

import main.kotlin.util.readInputForDay
import main.kotlin.util.safeSubList
import main.kotlin.util.safeSubSequence

fun main() {
    val lines = readInputForDay(11)
    val slPart1 = SeatingLayoutEngine(lines)
    //  val numIterations = sl.iterateTillStable()
    slPart1.iterateTillStable(slPart1::getSurroundingWithCurrent)
    val occupied1 = slPart1.countOccupied()
    println(occupied1)
    println(slPart1.toString())

    val slPart2 = SeatingLayoutEngine(lines)
    slPart2.iterateTillStable(slPart2::getSurroundingIgnoringEmptySeats)
    val occupied2 = slPart2.countOccupied()
    println(occupied2)
    println(slPart2.toString())


}

//fun main() {
//    val lines = readOtherInputForDay(11, "findseat")
//    val sl = SeatingLayoutEngine(lines, 1)
//    println(sl.getSurroundingIgnoringEmptySeats(3, 4))
//}

class SeatingLayoutEngine(var grid: List<String>, private val maxOccSurrounding: Int = 4) {

    //currentPosition is empty what is this seat in the next iteration
    private fun emptyRule(x: Int, y: Int, surroundFun: (x: Int, y: Int) -> List<Char>): Char {
        val occupy = surroundFun(x, y).all { it == 'L' || it == '.' }
        return if (occupy) '#' else 'L'
    }

    private fun occupiedRule(x: Int, y: Int, surroundFun: (x: Int, y: Int) -> List<Char>): Char {
        //add 1 to maxOccupiedSurrounding because we have counted our own seat
        val empty = surroundFun(x, y).count { '#' == it } >= (maxOccSurrounding + 1)
        return if (empty) 'L' else '#'
    }

    fun getSurroundingWithCurrent(x: Int, y: Int): List<Char> {
        return grid.safeSubList(y - 1, y + 2).map { it.safeSubSequence(x - 1, x + 2).toList() }.flatten().toList()
    }

    fun getSurroundingIgnoringEmptySeats(x: Int, y: Int): List<Char> {
        //left
        val left = grid[y].safeSubSequence(0, x).reversed().firstOrNull { it != '.' }
        val right = grid[y].safeSubSequence(x + 1, grid[y].length).firstOrNull { it != '.' }
        val up = grid.safeSubList(0, y).map { it[x] }.reversed().firstOrNull { it != '.' }
        val down = grid.safeSubList(y + 1, grid.size).map { it[x] }.firstOrNull { it != '.' }
        val leftUp = findLeftUp(x, y)
        val rightUp = findRightUp(x, y)
        val leftDown = findLeftDown(x, y)
        val rightDown = findRightDown(x, y)
        return listOfNotNull(left, right, up, down, leftUp, rightUp, leftDown, rightDown)
    }

    private fun findRightDown(x: Int, y: Int): Char? {
        var i = x
        var j = y
        while (true) {
            i += 1
            j += 1
            if (i >= grid[y].length || j >= grid.size) {
                break
            }
            val seat = grid[j][i]
            if (seat != '.') {
                return seat
            }
        }
        return null
    }

    private fun findLeftDown(x: Int, y: Int): Char? {
        var i = x
        var j = y
        while (true) {
            i -= 1
            j += 1
            if (i < 0 || j >= grid.size) {
                break
            }
            val seat = grid[j][i]
            if (seat != '.') {
                return seat
            }
        }
        return null
    }

    private fun findRightUp(x: Int, y: Int): Char? {
        var i = x
        var j = y
        while (true) {
            i += 1
            j -= 1
            if (i >= grid[y].length || j < 0) {
                break
            }
            val seat = grid[j][i]
            if (seat != '.') {
                return seat
            }
        }
        return null
    }

    private fun findLeftUp(x: Int, y: Int): Char? {
        var i = x
        var j = y
        while (true) {
            i -= 1
            j -= 1
            if (i < 0 || j < 0) {
                break
            }
            val seat = grid[j][i]
            if (seat != '.') {
                return seat
            }
        }
        return null
    }

    fun countOccupied(): Int {
        return grid.map { l -> l.count { '#' == it } }.sum()
    }

    fun iterateTillStable(surroundFun: (x: Int, y: Int) -> List<Char>) {
        var previousGrid = grid
        this.iterateOnce(surroundFun)
        while (previousGrid != grid) {
            previousGrid = grid
            this.iterateOnce(surroundFun)
        }
    }

    fun iterateOnce(surroundFun: (x: Int, y: Int) -> List<Char>) {
        val nextIt = mutableListOf<String>()
        for ((y, l) in grid.withIndex()) {
            var nextLine = ""
            for ((x, c) in l.withIndex()) {
                val next = when (c) {
                    'L' -> emptyRule(x, y, surroundFun)
                    '#' -> occupiedRule(x, y, surroundFun)
                    else -> c
                }
                nextLine += next
            }
            nextIt.add(nextLine)
        }
        grid = nextIt
    }


    override fun toString(): String {
        return grid.joinToString("\n")
    }
}
