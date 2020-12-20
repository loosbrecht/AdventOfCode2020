package main.kotlin.day20

import main.kotlin.util.readSmallInputForDay

fun main() {
    val lines = readSmallInputForDay(20)
    val tiles = parseInput(lines)

    tiles.forEach { it.countAllEdgesInCommon(tiles) }
    val productPart1 = tiles.filter { it.commonEdges == 2 }.map { it.tileId.toLong() }.reduce(Long::times)
    println(productPart1)

    tiles.forEach { it.countEdgesAndKeepNeighbours(tiles) }
    println()
    val img = Image(tiles.toMutableList())
}

fun parseInput(lines: List<String>): List<Tile> {
    var id = 0
    var image = mutableListOf<List<Char>>()
    val tiles = mutableListOf<Tile>()
    for (l in lines) {
        if (l.startsWith("Tile")) {
            tiles.add(Tile(id, image))
            image = mutableListOf()
            id = l.split(" ")[1].dropLast(1).toInt()
            continue
        } else if (l == "") {
            continue
        }
        image.add(l.toCharArray().toList())
    }
    tiles.add(Tile(id, image))
    return tiles.drop(1)
}


class Image(val tiles: MutableList<Tile>) {
    var tiledImage: MutableList<MutableList<Tile>> = mutableListOf()

    init {
        createFullTiledImage()
    }

    private fun createFullTiledImage() {
        val tilesAdded = mutableListOf<Tile>()
        var nxt = tiles.first { it.commonEdges == 2 }
        tilesAdded.add(nxt)
        var line = mutableListOf(nxt)
        while (true) {
            for (neigh in nxt.neighbours) {
                val tileRightOf = nxt.getNeighbourTileRightOf(neigh) ?: continue
                line.add(neigh)
                tilesAdded.add(neigh)
                break
            }

            while (true) {
                nxt = line.last()
                tilesAdded.add(nxt)
                nxt.neighbours.removeAll(tilesAdded)
                if (nxt.neighbours.isEmpty()) {
                    break
                } else {
                    for (neigh in nxt.neighbours) {
                        val tileRightOf = nxt.getNeighbourTileRightOf(neigh) ?: continue
                        line.add(neigh)
                        break
                    }
                }
            }
            tiledImage.add(line)
            nxt=line.first().neighbours[0]
            line = mutableListOf(nxt)
        }

    }


}


class Tile(val tileId: Int, val image: List<List<Char>>) {
    //contains the normal edges and the flipped edges
    private val edges: List<List<Char>>
    var commonEdges = 0

    var neighbours: MutableList<Tile> = mutableListOf()

    init {
        if (image.isNotEmpty()) {
            val tmpEdges = createEdges()
            edges = tmpEdges + tmpEdges.map { it.reversed() }
        } else {
            edges = emptyList()
        }
    }

    private fun createEdges(): List<List<Char>> {
        return listOf(
            image.first(),
            image.last(),
            image.map { it.first() }.toList(),
            image.map { it.last() }.toList()
        )
    }

    fun countEdgesAndKeepNeighbours(otherTiles: List<Tile>) {
        this.neighbours = otherTiles.filterNot { this == it }.filter { countCommonEdges(it.edges) > 0 }.toMutableList()
    }

    fun countAllEdgesInCommon(otherTiles: List<Tile>) {
        val initialCount = otherTiles.filterNot { this == it }.sumOf { countCommonEdges(it.edges) }
        //each common edge is counted twice
        this.commonEdges = initialCount / 2
    }

    private fun countCommonEdges(otherEdges: List<List<Char>>): Int {
        var count = 0
        for (mine in edges) {
            for (other in otherEdges) {
                if (mine == other) {
                    count++
                }
            }
        }
        return count
    }

    fun top(): List<Char> {
        return this.image.first()
    }

    fun bottom(): List<Char> {
        return this.image.last()
    }

    fun left(): List<Char> {
        return this.image.map { it.first() }
    }

    fun right(): List<Char> {
        return this.image.map { it.last() }
    }

    fun getNeighbourTileRightOf(other: Tile): Tile? {
        val right = this.right()
        if (right == other.left()) {
            return other
        } else if (right == other.left().reversed()) {
            return other.flipped()
        } else if (right == other.top()) {
            return other.rotateLeft()
        } else if (right == other.bottom()) {
            return other.rotateRight()
        }
        return null
    }

    fun rotateLeft(): Tile {
        val size = this.image.size
        val newImage = Array(size) { CharArray(size) }
        for (i in 0 until size) {
            for (j in 0 until size) {
                newImage[i][j] = newImage[j][size - i - 1]
            }
        }
        val mutList = mutableListOf<List<Char>>()
        for (imgLine in newImage) {
            mutList.add(imgLine.toList())
        }
        return Tile(this.tileId, mutList)
    }

    fun rotateRight(): Tile {
        val size = this.image.size
        val newImage = Array(size) { CharArray(size) }
        for (i in 0 until size) {
            for (j in 0 until size) {
                newImage[i][j] = this.image[size - j - 1][size - i]
            }
        }
        val mutList = mutableListOf<List<Char>>()
        for (imgLine in newImage) {
            mutList.add(imgLine.toList())
        }
        return Tile(this.tileId, mutList)
    }

    private fun flipped(): Tile {
        return Tile(this.tileId, this.image.reversed())
    }

}