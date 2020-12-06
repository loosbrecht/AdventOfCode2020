package main.kotlin.day6

import main.kotlin.util.readInputForDay


fun main() {

    val lines = readInputForDay(6)
    val totalAnswers = collectAllAnswers(lines).map { countAnswersFromGroup(it) }.sum()
    val totalAnswersInCommon = collectAllAnswers(lines).map { countAnswersInCommon(it) }.sum()
    println(totalAnswers)
    println(totalAnswersInCommon)
}

fun collectAllAnswers(lines: List<String>): List<List<String>> {
    val groups = mutableListOf<List<String>>()
    var persons = mutableListOf<String>()
    for (line in lines) {
        if (line.isEmpty()) {
            groups.add(persons)
            persons = mutableListOf()
            continue
        }
        persons.add(line)
    }
    groups.add(persons)
    return groups
}

fun countAnswersInCommon(groups: List<String>): Int {
    val memberCount = groups.size
    return ('a'..'z').count { ch -> groups.filter { it.contains(ch) }.count() == memberCount }
}

fun countAnswersFromGroup(group: List<String>): Int {
    val answers = mutableMapOf<Char, Boolean>()
    group.forEach { p -> p.forEach { answers[it] = true } }
    return answers.count()
}


