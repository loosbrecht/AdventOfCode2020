package main.kotlin.day3

import java.io.File

fun main() {
    val allLines = readFileToLines("input/day3.txt")
    val stepList = listOf<Steps>(
        Steps(1, 1),
        Steps(3, 1), //part1
        Steps(5, 1),
        Steps(7, 1),
        Steps(1, 2)
    )
    val total = stepList.map { s -> findAllTreesOnThePath(allLines, s) }.reduce(Int::times)
    println(total)
}

private fun findAllTreesOnThePath(allLines: List<String>, step: Steps): Int {
    var countTrees = 0
    var pos = Pos(0, 0, step, allLines[0].length)
    while (true) {
        pos = pos.getNextPos()
        if (pos.y >= allLines.size) {
            break
        }
        if (isATree(pos, allLines)) {
            countTrees++
        }
    }
    println(countTrees)
    return countTrees
}

data class Steps(val right: Int, val down: Int)

class Pos(var x: Int, var y: Int, val step: Steps, private val rightLimit: Int) {
    fun getNextPos(): Pos {
        var nextX = x + step.right
        if (nextX > rightLimit - 1) {
            nextX -= rightLimit
        }
        val nextY = y + step.down
        return Pos(nextX, nextY, step, rightLimit)
    }

    override fun toString(): String {
        return "pos: $x $y"
    }
}

fun isATree(pos: Pos, allLines: List<String>): Boolean {
    return allLines[pos.y][pos.x] == '#'
}

fun readFileToLines(fileName: String): List<String> {
    return File(fileName).readLines()
}