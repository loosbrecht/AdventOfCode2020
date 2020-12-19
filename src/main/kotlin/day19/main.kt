package main.kotlin.day19

import main.kotlin.util.readInputForDay

fun main() {
    val lines = readInputForDay(19)
    val idx = lines.indexOfFirst { it == "" }
    val ruleInput = lines.subList(0, idx).toMutableList()
    val input = lines.subList(idx + 1, lines.size)

    if (true) {
        val idx8 = ruleInput.indexOfFirst { it.startsWith("8:") }
        val idx11 = ruleInput.indexOfFirst { it.startsWith("11:") }
        ruleInput[idx8] = "8: 42 | 42 8"
        ruleInput[idx11] = "11: 42 31 | 42 11 31"
    }


    val rules = parseRules(ruleInput)
    val regex = rules[0]!!.buildRegex(1)
    println(regex)
    val reg = Regex(regex)
    val amount = input.count { evaluateInput(it, reg) }
    println(amount)
}

fun evaluateInput(s: String, reg: Regex): Boolean {

    val result = reg.matches(s)
    println("$s $result")
    return result
}

fun parseRules(lines: List<String>): MutableMap<Int, Rules> {
    val allRules = mutableMapOf<Int, Rules>()
    for (l in lines) {
        val id = l.split(":")[0].toInt()
        val rules = l.split(":")[1]
        if (rules.contains("a")) {
            allRules[id] = EndRule(id, "a")
        } else if (rules.contains("b")) {
            allRules[id] = EndRule(id, "b")
        } else if (rules.contains("|")) {
            val or = mutableListOf<List<Int>>()
            val sides = rules.split("|")
            for (s in sides) {
                or.add(s.split(" ").filter { it != "" }.map { it.toInt() })
            }
            allRules[id] = OrRules(id, or)
        } else {
            val order = rules.split(" ").filter { it != "" }.map { it.toInt() }
            allRules[id] = OrderedRules(id, order)
        }
    }
    for ((k, v) in allRules) {
        when (v) {
            is EndRule -> continue
            is OrderedRules -> {
                v.orderedRules = v.ids.map { allRules[it]!! }.toList()
            }
            is OrRules -> {
                v.left = v.ids[0].map { allRules[it]!! }.toList()
                v.right = v.ids[1].map { allRules[it]!! }.toList()
            }
        }
    }
    return allRules
}

interface Rules {
    fun buildRegex(depth: Int): String
}

class EndRule(val id: Int, val value: String) : Rules {
    override fun buildRegex(depth: Int): String {
        return value
    }


}

class OrderedRules(val id: Int, val ids: List<Int>) : Rules {
    lateinit var orderedRules: List<Rules>
    override fun buildRegex(depth: Int): String {
        if (depth == 100) {
            return ""
        }
        return orderedRules.joinToString(separator = "") { it.buildRegex(depth + 1) }
    }


}

class OrRules(val id: Int, val ids: List<List<Int>>) : Rules {
    lateinit var left: List<Rules>
    lateinit var right: List<Rules>
    override fun buildRegex(depth: Int): String {
        if (depth == 100) {
            return ""
        }
        val l = left.joinToString(separator = "") { it.buildRegex(depth + 1) }
        val r = right.joinToString(separator = "") { it.buildRegex(depth + 1) }
        return "($l|$r)"
    }
}