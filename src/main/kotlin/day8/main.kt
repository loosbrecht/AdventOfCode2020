package main.kotlin.day8

import main.kotlin.util.readInputForDay
import main.kotlin.util.readSmallInputForDay


fun main() {
    val lines = readInputForDay(8)
    val accumulator = executeLines(lines)
    println(accumulator)
    val fixedAccumulator = fixLinesAndExecute(lines)
    println(fixedAccumulator)
}

fun fixLinesAndExecute(lines: List<String>): Int {
    val changed = mutableMapOf<Int, Boolean>()
    var newLines = lines
    var acc = 0
    while (true) {
        acc = executeLines(newLines, true)
        if (acc != -1) {
            break
        }
        //change the next jmp or nop
        newLines = fixCommandList(lines, changed, newLines)

    }
    return acc
}

fun fixCommandList(
    lines: List<String>,
    changed: MutableMap<Int, Boolean>,
    newLines: List<String>
): List<String> {
    val changedList = mutableListOf<String>()
    changedList.addAll(lines)
    for ((i, l) in lines.withIndex()) {
        if (changed.containsKey(i)) {
            continue
        }
        if (l.contains("jmp")) {
            // println("change line $i to nop")
            changedList[i] = l.replace("jmp", "nop")
            changed[i] = true
            break
        } else if (l.contains("nop")) {
            // println("change line $i to jmp")
            changedList[i] = newLines[i].replace("nop", "jmp")
            changed[i] = true
            break
        }
    }
    return changedList
}


fun executeLines(lines: List<String>, failOnLoop: Boolean = false): Int {
    var accumulator = 0;
    val alreadyVisited = mutableMapOf<Int, Boolean>()
    var exeLine = 0
    while (true) {
        if (alreadyVisited.containsKey(exeLine)) {
            if (failOnLoop) {
                return -1
            } else {
                break
            }
        }
        if(exeLine >= lines.size){
            break
        }
        val cmds = lines[exeLine].split(" ")
        alreadyVisited[exeLine] = true
        when (cmds[0]) {
            "nop" -> exeLine++
            "acc" -> {
                accumulator += cmds[1].toInt()
                exeLine++
            }
            "jmp" -> {
                exeLine += cmds[1].toInt()
            }
        }
    }
    return accumulator
}