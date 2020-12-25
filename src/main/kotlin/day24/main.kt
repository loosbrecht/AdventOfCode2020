package main.kotlin.day24

import main.kotlin.util.readSmallInputForDay
import main.kotlin.util.safeSubList

fun main() {
    val lines = readSmallInputForDay(24)
    val flippedTiles = flipTilesBasedOnInput(lines)
    println(flippedTiles.count { it.colour == Colour.BLACK })
    val flippedTilesAlive = gameOfLifeWithTiles(flippedTiles)
    println(flippedTilesAlive.count { it.colour == Colour.BLACK })


}


fun flipTilesBasedOnInput(input: List<String>): List<Tile> {
    val tiles = mutableListOf<Tile>()

    for (inp in input) {
        val newTile = flipOneTile(inp)
        val tileFound = tiles.find { it.x == newTile.x && it.y == newTile.y }
        if (tileFound == null) {
            tiles.add(newTile)
        } else {
           // tileFound.colour = tileFound.colour.flip()
        }
    }


    return tiles
}

fun flipOneTile(inp: String): Tile {
    var x = 0
    var y = 0
    var input = inp.toList()
    while (input.isNotEmpty()) {
        val buf = input.safeSubList(0, 2)
        var bufStr = buf.joinToString(separator = "")
        when (bufStr) {
            "ne" -> {
                x += 1
                y += 1
            }
            "se" -> {
                x += 1
                y -= 1
            }
            "sw" -> {
                x -= 1
                y -= 1
            }
            "nw" -> {
                x -= 1
                y += 1
            }
            else -> {
                bufStr = buf[0].toString()
                when (bufStr) {
                    "e" -> {
                        x += 2
                    }
                    "w" -> {
                        x -= 2
                    }
                }
            }
        }
        input = input.drop(bufStr.length)
    }
    return Tile(x, y, Colour.BLACK)
}


fun gameOfLifeWithTiles(flippedTiles: List<Tile>): List<Tile> {
    var tileMap = mutableMapOf<Pair<Int, Int>, Tile>()
    for (tile in flippedTiles) {
        tileMap[Pair(tile.x, tile.y)] = tile
    }
    printMap(tileMap)
    for (c in 1..100) {
        val newTileMap = mutableMapOf<Pair<Int, Int>, Tile>()
        val xMin = tileMap.map { it.key.first }.minOrNull()!!
        val xMax = tileMap.map { it.key.first }.maxOrNull()!!
        val yMin = tileMap.map { it.key.second }.minOrNull()!!
        val yMax = tileMap.map { it.key.second }.maxOrNull()!!
        for (x in xMin - 2..xMax + 2) {
            for (y in yMin - 2..yMax + 2) {
                val blackC = countNeigbhours(tileMap, x, y)
                val tile = tileMap[Pair(x, y)]
                val colour = if (tile != null) {
                    when (tile.colour) {
                        Colour.BLACK -> if (blackC == 0 || blackC > 2) Colour.WHITE else Colour.BLACK
                        Colour.WHITE -> if (blackC == 2) Colour.BLACK else Colour.WHITE
                    }
                } else {
                    Colour.WHITE
                }
                newTileMap[Pair(x, y)] = Tile(x, y, colour)

            }

        }
        tileMap = newTileMap
        //printMap(tileMap)
        println(tileMap.values.count { it.colour == Colour.BLACK })
    }
    return tileMap.values.toList()
}

fun printMap(tileMap: MutableMap<Pair<Int, Int>, Tile>) {

    val xMin = tileMap.map { it.key.first }.minOrNull()!!
    val xMax = tileMap.map { it.key.first }.maxOrNull()!!
    val yMin = tileMap.map { it.key.second }.minOrNull()!!
    val yMax = tileMap.map { it.key.second }.maxOrNull()!!
    for (y in yMin..yMax) {
        for (x in xMin..xMax) {
            val tile = tileMap[Pair(x, y)]
            if (tile == null)
                print("_")
            else {
                when (tile.colour) {
                    Colour.WHITE -> print("_")
                    Colour.BLACK -> print("B")
                }
            }
        }
        println()
    }

}

fun countNeigbhours(tileMap: MutableMap<Pair<Int, Int>, Tile>, x: Int, y: Int): Int {
    val neighbours = mutableListOf<Tile?>()
    neighbours.add(tileMap[Pair(x + 1, y + 1)])
    neighbours.add(tileMap[Pair(x + 1, y - 1)])
    neighbours.add(tileMap[Pair(x - 1, y - 1)])
    neighbours.add(tileMap[Pair(x - 1, y + 1)])
    neighbours.add(tileMap[Pair(x - 2, y)])
    neighbours.add(tileMap[Pair(x + 2, y)])

    var blackCount = 0
    for (n in neighbours.filterNotNull()) {
        if (n.colour == Colour.BLACK) blackCount++
    }
    return blackCount
}


data class Tile(val x: Int, val y: Int, var colour: Colour)

enum class Colour {
    WHITE, BLACK;

    fun flip(): Colour {
        return when (this) {
            WHITE -> BLACK
            BLACK -> WHITE
        }
    }
}
