package main.kotlin.day7

import main.kotlin.util.readInputForDay

const val shinyGold = "shiny gold"

fun main() {
    val bags = readInputForDay(7).map { parse(it) }.toSet()
    val totalContaining = findAllBagsContainingShinyGold(bags)
    println(totalContaining.size)

    val numOfBags = calculateNumberOfBagsInShinyGold(bags)
    println(numOfBags)
}


fun calculateNumberOfBagsInShinyGold(bags: Set<Bag>): Int {
    return calculateNumberOfBags(bags, shinyGold) - 1
}

//count all bags in the bag with this colour, we also count the bag itself
fun calculateNumberOfBags(bags: Set<Bag>, colour: String): Int {
    val bag = bags.find { it.colour == colour }!!
    var total = 1
    for ((k, v) in bag.contents) {
        total += (v.amount * calculateNumberOfBags(bags, v.colour))
    }
    return total
}

fun findAllBagsContainingShinyGold(bags: Set<Bag>): Set<String> {
    val totalContaining = mutableSetOf<String>()

    var searchList = setOf(shinyGold)
    while (true) {
        val found = mutableSetOf<String>()
        for (search in searchList) {
            found.addAll(findBagContaining(bags, search))
        }
        if (found.isEmpty()) {
            break
        }
        searchList = found
        totalContaining.addAll(found)
    }
    return totalContaining
}

fun findBagContaining(bags: Set<Bag>, contains: String): List<String> {
    return bags.filter { it.contents.containsKey(contains) }.map { it.colour }
}


//worst function ever
fun parse(line: String): Bag {
    val containSplit = line.split(" contain ")
    val colour = containSplit[0].dropLast(5)
    val inside = containSplit[1].split(", ")
    if (inside.size == 1) {
        if (inside[0] == "no other bags.") {
            return Bag(colour, mapOf())
        }
    }
    val mutMap = mutableMapOf<String, Content>()
    for (ins in inside) {
        val str = ins.replace(".", "", false)
        val inSplit = str.split(" ")
        val content = inSplit[1] + " " + inSplit[2]
        mutMap[content] = Content(content, inSplit[0].toInt())
    }
    return Bag(colour, mutMap)
}

data class Content(val colour: String, val amount: Int)
//content map key has the same value as colour
data class Bag(val colour: String, val contents: Map<String, Content>)