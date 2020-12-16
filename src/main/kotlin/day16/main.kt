package main.kotlin.day16

import main.kotlin.util.readInputForDay
import java.util.*

fun main() {
    val lines = readInputForDay(16)
    val parsed = parseLines(lines)
    val rules = parsed.first
    val myValues = parsed.second.first
    val otherPeopleValues = parsed.second.second

    val (part1, validOtherPeopleValues) = checkErrorsInNearbyTickets(rules, otherPeopleValues)
    println(part1)

    val fixedRules = findTheIndexForEachRule(rules, validOtherPeopleValues)
    val part2 = calcDepartureMul(fixedRules, myValues)
    println(part2)
}

private fun calcDepartureMul(
    fixedRules: List<FieldWithRules>,
    myValues: List<Int>
): Long {
    val departures = fixedRules.filter { it.name.startsWith("departure") }
    var mul = 1L
    departures
        .asSequence()
        .map { it.idxCandidate[0] }
        .forEach { mul *= myValues[it] }
    return mul
}

fun findTheIndexForEachRule(
    rules: List<FieldWithRules>,
    validOtherPeopleValues: List<List<Int>>
): List<FieldWithRules> {
    val max = validOtherPeopleValues.size
    for (r in rules) {
        for (i in 0 until 20) {
            val count = validOtherPeopleValues.map { it[i] }.count { r.valid(it) }
            if (count == max) {
                r.idxCandidate.add(i)
            }
        }
    }
    for (a in 0 until 20) {
        //rules.forEach { println("${it.name} ${it.idxCandidate}") }
        val fixedRules = rules.filter { it.idxCandidate.size == 1 }.map { it.idxCandidate[0] }
        rules
            .asSequence()
            .filter { it.idxCandidate.size > 1 }
            .forEach { it.idxCandidate.removeAll(fixedRules) }
    }
    return rules
}


//do in a stream
fun checkErrorsInNearbyTickets(
    rules: List<FieldWithRules>,
    otherPeopleValues: List<List<Int>>
): Pair<Int, List<List<Int>>> {
    val validList = mutableListOf<List<Int>>()
    var invalidValues = 0
    for (people in otherPeopleValues) {
        var valid = true
        peopleVal@ for (v in people) {
            for (r in rules) {
                if (r.valid(v)) {
                    continue@peopleVal
                }
            }
            valid = false
            invalidValues += v
        }//v in people
        if (valid) {
            validList.add(people)
        }
    }// people

    return Pair(invalidValues, validList)
}


fun parseLines(lines: List<String>): Pair<List<FieldWithRules>, Pair<List<Int>, List<List<Int>>>> {
    var fields = true
    var yourTicket = false
    var myTickets = listOf<Int>()
    var otherTickets = mutableListOf<List<Int>>()
    val fieldList = mutableListOf<FieldWithRules>()
    for (line in lines) {
        if (fields) {
            if (line == "") {
                fields = false
                continue
            }
            val name = line.split(":")[0]
            val rules = line.split(":")[1].split(" ")
            val mutRules = mutableListOf<Range>()
            for (r in rules) {
                if (r == "" || r == "or") {
                    continue
                }
                val split = r.split("-")
                mutRules.add(Range(split[0].toInt(), split[1].toInt()))
            }
            val f = FieldWithRules(name, mutRules)
            fieldList.add(f)
            continue
        }
        if (line == "") {
            continue
        } else if (line == "your ticket:") {
            yourTicket = true
            continue
        } else if (line == "nearby tickets:") {
            yourTicket = false
            continue
        }

        if (yourTicket) {
            myTickets = line.split(",").map { it.toInt() }.toList()
        } else {
            otherTickets.add(line.split(",").map { it.toInt() }.toList())
        }

    }
    return Pair(fieldList, Pair(myTickets, otherTickets))
}


data class Range(val min: Int, val max: Int)

class FieldWithRules(val name: String, private val rangeList: List<Range>) {
    var idxCandidate = mutableListOf<Int>()

    fun valid(v: Int): Boolean {
        for (r in rangeList) {
            if (v >= r.min && v <= r.max) {
                return true
            }
        }
        return false
    }
}