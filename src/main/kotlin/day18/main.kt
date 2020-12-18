package main.kotlin.day18

import main.kotlin.util.readInputForDay

fun main() {
   // println(calculateWholeExpr("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))
    // println(calculateWholeExpr("((4 + 4 + 4 + 4 * 5 + 4) * (2 * 9) * 6 + 5) + ((6 + 3 * 6) + 6 * (3 * 5 + 7) + 9) + 2 + 8 * 4 * (6 + 2 * 5 + 8)"))
    val lines = readInputForDay(18)
    val sum:Long = lines.map { calculateWholeExpr(it) }.sum()
    println(sum)
}

fun calculateWholeExpr(line: String): Long {
    val elements = line.split(" ")
    val res = calculateExpr(elements.iterator())
    println("Elements: $line \nResult: $res")
    return res
}

fun calculateExpr(it: Iterator<String>): Long {
    var total = 0L
    while (it.hasNext()) {
        val s = it.next()
        if (s.startsWith("(")) {
            total = handleParentheses(s, it)
        } else if (s == "+") {
            total += calculateNextExpr(it)
        } else if (s == "*") {
            total *= calculateNextExpr(it)
        } else {
            total = s.toLong()
        }
    }
    return total
}

fun calculateNextExpr(it: Iterator<String>): Long {
    var total = 0L
    while (it.hasNext()) {
        var s = it.next()
        if (s.startsWith("(")) {
            return handleParentheses(s, it)
        } else if (s == "+") {
            return total + calculateExpr(it)
        } else if (s == "*") {
            return total * calculateExpr(it)
        } else {
            s = s.replace(")", "")
            return s.toLong()
        }
    }
    return total
}

fun handleParentheses(s: String, it: Iterator<String>): Long {
    val inner = mutableListOf<String>()
    var total = 0L
    var count = s.count { it == '(' }
    inner.add(s)
    while (it.hasNext() && count != 0) {
        var str = it.next()
        val open = str.count { it == '(' }
        val closed = str.count { it == ')' }
        count = count + open - closed
        inner.add(str)
    }
    var joined = inner.joinToString(" ")
    while (joined.contains("(") || joined.contains(")")) {
        var substr = joined.substring(joined.lastIndexOf("("))
        substr = substr.substring(1, substr.indexOf(")"))

        val res = calculateExpr(substr.split(" ").iterator())
        joined = joined.replace("(" + substr + ")", res.toString())
    }
    return joined.toLong()
}

