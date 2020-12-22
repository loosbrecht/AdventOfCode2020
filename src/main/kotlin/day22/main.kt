package main.kotlin.day22

import main.kotlin.util.readInputForDay
import java.util.*

fun main() {
    val lines = readInputForDay(22)
    var players = parseInput(lines)
    val score = playGame(players.first, players.second)
    println(score)
    players = parseInput(lines)
    val scorePart2 = playRecursiveGame(players.first, players.second)
    println(scorePart2)

}

enum class Player {
    ONE, TWO
}

fun playRecursiveGame(player1: Queue<Int>, player2: Queue<Int>): Long {
    return when (playRecursiveRound(player1, player2)) {
        Player.ONE -> getScore(player1)
        Player.TWO -> getScore(player2)
    }
}

fun playRecursiveRound(player1: Queue<Int>, player2: Queue<Int>): Player {
    val history = mutableListOf<Queue<Int>>()
    while (true) {
        if (history.contains(player1)) {
            return Player.ONE
        }
        history.add(copy(player1))

        if (player1.isEmpty() || player2.isEmpty()) {
            break
        }
        val play1 = player1.poll()
        val play2 = player2.poll()
        if (player1.size >= play1 && player2.size >= play2) {
            when (playRecursiveRound(getSubQueue(player1, play1), getSubQueue(player2, play2))) {
                Player.ONE -> {
                    player1.add(play1)
                    player1.add(play2)
                }
                Player.TWO -> {
                    player2.add(play2)
                    player2.add(play1)
                }
            }
            continue
        }
        if (play1 > play2) {
            player1.add(play1)
            player1.add(play2)
        } else {
            player2.add(play2)
            player2.add(play1)
        }
    }
    return if (player1.isNotEmpty()) Player.ONE else Player.TWO
}

fun getSubQueue(queue: Queue<Int>, size: Int): Queue<Int> {
    val newQueue: Queue<Int> = LinkedList<Int>()
    newQueue.addAll(queue.toMutableList().subList(0, size))
    return newQueue
}

fun copy(queue: Queue<Int>): Queue<Int> {
    val newQueue: Queue<Int> = LinkedList<Int>()
    newQueue.addAll(queue.toMutableList())
    return newQueue
}

fun playGame(player1: Queue<Int>, player2: Queue<Int>): Long {
    while (true) {
        if (player1.isEmpty() || player2.isEmpty()) {
            break
        }
        val play1 = player1.poll()
        val play2 = player2.poll()

        if (play1 > play2) {
            player1.add(play1)
            player1.add(play2)
        } else {
            player2.add(play2)
            player2.add(play1)
        }
    }
    return if (player1.isNotEmpty()) getScore(player1) else getScore(player2)
}

fun getScore(cards: Queue<Int>): Long {
    return cards.reversed()
        .mapIndexed { i, c -> ((i + 1) * c).toLong() }
        .sum()
}


fun parseInput(lines: List<String>): Pair<Queue<Int>, Queue<Int>> {
    val player1: Queue<Int> = LinkedList<Int>()
    val player2: Queue<Int> = LinkedList<Int>()
    val it = lines.iterator()
    it.next() //skip first line
    while (it.hasNext()) {
        val s = it.next()
        if (s == "") {
            break
        }
        player1.add(s.toInt())
    }
    it.next()
    while (it.hasNext()) {
        val s = it.next()
        player2.add(s.toInt())
    }
    return Pair(player1, player2)
}
