package main.kotlin.day15

import java.util.*

fun main() {
    val gameNumbers = mutableListOf<Int>(19,20,14,0,9,1)
    val part1 = playPart1(gameNumbers, 2020)
    println(part1)
    val part2 = playPart1(gameNumbers, 30000000)
    println(part2)
}

private fun playPart1(gameNumbers: MutableList<Int>, numInList: Int): Int {
    val initList = gameNumbers.size
    var lastPlayed = gameNumbers[0]
    val indexLookup = mutableMapOf<Int, LimitedSizeQueue>()
    for (i in 0 until numInList) {
        if (i < initList) {
            lastPlayed = gameNumbers[i]
            val queue = LimitedSizeQueue(2)
            queue.put(i)
            indexLookup[lastPlayed] = queue
            continue
        }

        if (indexLookup[lastPlayed]!!.size() == 1) {
            lastPlayed = 0
        } else {
            val indexes = indexLookup[lastPlayed]!!.values()
            lastPlayed = indexes[1] - indexes[0]
        }
        if (!indexLookup.containsKey(lastPlayed)) {
            val queue = LimitedSizeQueue(2)
            queue.put(i)
            indexLookup[lastPlayed] = queue
        } else {
            indexLookup[lastPlayed]?.put(i)
        }
        gameNumbers.add(lastPlayed)
       // println("$i : $lastPlayed")
    }
    return gameNumbers.last()
}

class LimitedSizeQueue(val size: Int) {
    private val queue: Queue<Int>

    init {
        queue = LinkedList<Int>()
    }

    fun put(value: Int) {
        if (queue.size >= size) {
            queue.remove()
        }
        queue.add(value)
    }

    fun values(): List<Int> {
        return queue.toList()
    }

    fun size(): Int {
        return queue.size
    }
}