package main.kotlin.day12

import main.kotlin.util.readInputForDay
import main.kotlin.util.readSmallInputForDay
import kotlin.math.abs

fun main() {
    val lines = readInputForDay(12)

    val dist = travelRoute(lines)
    println(dist)
    val dist2 = travelRouteWithWaypoint(lines)
    println(dist2)
}

fun travelRoute(lines: List<String>): Int {
    val currentPos = Position(Direction.EAST, 0, 0)
    lines.map { parseLine(it) }.forEach { (dir, dist) -> currentPos.applyNextCommand(dir, dist) }
    return currentPos.calcManhattanDistance()
}

private fun parseLine(line: String): Pair<Char, Int> {
    val dir = line[0]
    val dist = line.drop(1).toInt()
    return Pair(dir, dist)
}

fun travelRouteWithWaypoint(lines: List<String>): Int {
    val currentPos = PositionWithWayPoint(Position(Direction.EAST, 0, 0), 10, 1)
    lines.map { parseLine(it) }.forEach { (dir, dist) -> currentPos.applyNextCommand(dir, dist) }
    return currentPos.ship.calcManhattanDistance()
}


class PositionWithWayPoint(var ship: Position, var xWay: Int, var yWay: Int) {
    val order = listOf(Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH)

    fun applyNextCommand(dir: Char, dist: Int) {
        when (dir) {
            'N' -> this.yWay += dist
            'S' -> this.yWay -= dist
            'W' -> this.xWay -= dist
            'E' -> this.xWay += dist
            'L' -> {
                turnLeft(dist)
            }
            'R' -> {
                turnRight(dist)
            }
            'F' -> {
                val newX = this.xWay * dist
                val newY = this.yWay * dist
                ship.x += newX
                ship.y += newY
            }
        }
    }
    //clockwise
    fun turnRight(angle: Int) {
        when (angle) {
            90 -> {
                val tmp = xWay
                xWay = yWay
                yWay = -tmp
            }
            180 -> {
                xWay = -xWay
                yWay = -yWay
            }
            270 -> {
                val tmp = xWay
                xWay = -yWay
                yWay = tmp
            }
        }
    }
        //counterclockwise
    fun turnLeft(angle: Int) {
        when (angle) {
            90 -> {
                val tmp = xWay
                xWay = -yWay
                yWay = tmp
            }
            180 -> {
                xWay = -xWay
                yWay = -yWay
            }
            270 -> {
                val tmp = xWay
                xWay = yWay
                yWay = -tmp
            }
        }
    }
}


class Position(var dir: Direction, var x: Int, var y: Int) {
    val order = listOf(Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH)

    fun setNewDirection(angle: Int, s: String) {
        val amount = angle / 90
        val currentIndex = order.indexOf(dir)
        if (s == "L") {
            var newIndex = amount + currentIndex
            if (newIndex >= order.size) {
                newIndex -= order.size
            }
            dir = order[newIndex]
        } else if (s == "R") {
            var newIndex = currentIndex - amount
            if (newIndex < 0) {
                newIndex += order.size
            }
            dir = order[newIndex]
        }
    }

    fun applyNextCommand(dir: Char, dist: Int) {
        when (dir) {
            'N' -> this.y += dist
            'S' -> this.y -= dist
            'W' -> this.x -= dist
            'E' -> this.x += dist
            'L' -> this.setNewDirection(dist, "L")
            'R' -> this.setNewDirection(dist, "R")
            'F' -> applyNextCommand(this.dir.firstChar(), dist)
        }
    }

    fun calcManhattanDistance(): Int {
        return abs(x) + abs(y)
    }

}

enum class Direction {
    NORTH, SOUTH, WEST, EAST;

    fun firstChar(): Char {
        return this.toString()[0]
    }
}