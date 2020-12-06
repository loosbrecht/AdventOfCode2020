package main.kotlin.day4

import main.kotlin.util.readInputForDay
import java.io.File

fun main() {
    val batch = createPassportBatch(readInputForDay(4))
    val validPassportsPart1 = batch.filter { it.valid() }.count()
    println(validPassportsPart1)
    val validPassportsPart2 = batch.filter { it.valid() }.filter { it.validFields() }.count()
    println(validPassportsPart2)
}


class Passport(val fields: MutableMap<String, String>) {
    private val keysWithValidationFuncs: Map<String, (String) -> Boolean> = mapOf(
        "byr" to { it.toInt() in 1920..2002 },
        "iyr" to { it.toInt() in 2010..2020 },
        "eyr" to { it.toInt() in 2020..2030 },
        "hgt" to {
            when (it.takeLast(2)) {
                "cm" -> it.dropLast(2).toInt() in 150..193
                "in" -> it.dropLast(2).toInt() in 59..76
                else -> false
            }
        },
        "hcl" to { Regex("#[a-f0-9]{6}").matches(it) },
        "ecl" to { it in arrayOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") },
        "pid" to { Regex("[0-9]{9}").matches(it) },
        "cid" to { true })

    fun valid(): Boolean {
        return when (fields.size) {
            8 -> fields.keys.filter { keysWithValidationFuncs.containsKey(it) }.count() == 8
            7 -> fields.keys.filterNot { it == "cid" }.count() == 7
            else -> false
        }
    }

    fun validFields(): Boolean {
        for ((k, v) in fields) {
            if (!keysWithValidationFuncs[k]?.invoke(v)!!) {
                return false
            }
        }
        return true
    }
}

fun createPassportBatch(lines: List<String>): List<Passport> {
    val batch = mutableListOf<Passport>()
    var currentPassport = Passport(mutableMapOf())
    for (line in lines) {
        if (line.isEmpty()) {
            batch.add(currentPassport)
            currentPassport = Passport(mutableMapOf())
            continue
        }
        val splits = line.split(" ")
        splits.forEach {
            val sp = it.split(":")
            currentPassport.fields.put(sp[0], sp[1])
        }
    }
    batch.add(currentPassport)
    return batch
}