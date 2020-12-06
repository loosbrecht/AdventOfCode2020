package main.kotlin.util

import java.io.File

fun readInputForDay(day:Int):List<String>{
    return readLines("input/day$day/day$day.txt")
}
fun readSmallInputForDay(day:Int):List<String>{
    return readLines("input/day$day/day$day-small.txt")
}
fun readOtherInputForDay(day: Int,key:String):List<String>{
    return readLines("input/day$day/day$day-$key.txt")
}

private fun readLines(fileName: String): List<String> {
    return File(fileName).readLines()
}