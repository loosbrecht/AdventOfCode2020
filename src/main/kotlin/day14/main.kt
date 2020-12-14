package main.kotlin.day14


import main.kotlin.util.readInputForDay
import java.util.*
import kotlin.math.pow

fun main() {
    val lines = readInputForDay(14)
    val part1 = applyBitmaskToMemory(lines)
    println(part1)
    val part2 = applyBitmaskToMemoryV2(lines)
    println(part2)
}

fun applyBitmaskToMemory(lines: List<String>): Long {
    val memory = mutableMapOf<Int, BitSet>()
    var bitmask = ""
    var loc = -1
    var value = -1L
    for (line in lines) {
        val split = line.split(" = ")
        if (line.startsWith("mask")) {
            bitmask = split[1]
            continue
        } else {
            value = split[1].toLong()
            loc = split[0].replace("mem[", "").replace("]", "").toInt()
        }
        val bits = BitSet.valueOf(longArrayOf(value))
        for ((i, b) in bitmask.reversed().withIndex()) {
            when (b) {
                'X' -> continue
                '1' -> bits.set(i, true)
                '0' -> bits.set(i, false)
            }
        }
        memory[loc] = bits
    }
    return memory.map { (_, v) -> v.toLongArray()[0] }.sum()
}


fun applyBitmaskToMemoryV2(lines: List<String>): Long {
    val memory = mutableMapOf<Long, Long>()
    var bitmask = ""
    var loc = -1
    var value = -1L
    for (line in lines) {
        val split = line.split(" = ")
        if (line.startsWith("mask")) {
            bitmask = split[1]
            continue
        } else {
            value = split[1].toLong()
            loc = split[0].replace("mem[", "").replace("]", "").toInt()
        }
        val locBitset = BitSet.valueOf(longArrayOf(loc.toLong()))
        val powX = 2.0.pow(bitmask.filter { 'X' == it }.count().toDouble()).toInt()
        for (i in 0 until powX) {
            val adaptedBs: BitSet = locBitset.clone() as BitSet
            val bs = BitSet.valueOf(longArrayOf(i.toLong()))
            var bsIndex = 0
            for ((j, b) in bitmask.reversed().withIndex()) {
                when (b) {
                    'X' -> adaptedBs.set(j, bs.get(bsIndex++))
                    '1' -> adaptedBs.set(j, true)
                }
            }
            memory[adaptedBs.toLongArray()[0]] = value
        }
    }
    return memory.map { (_, v) -> v }.sum()
}