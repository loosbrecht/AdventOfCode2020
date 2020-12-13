import main.kotlin.util.readInputForDay
import main.kotlin.util.readSmallInputForDay
import java.lang.Math.abs

fun main() {
    val lines = readInputForDay(13)
    val part1 = findFirstBus(lines)
    println(part1)


}


fun findFirstBus(lines: List<String>): Int {
    val earliestLeaveTs = lines[0].toInt()
    val busIDList = lines[1].split(",").filterNot { it == "x" }.map { it.toInt() }
    val closestToStartPoint = busIDList.map { earliestLeaveTs / it }.mapIndexed { index, it -> it * busIDList[index] }
    val afterStartPoint =
        closestToStartPoint.mapIndexed { index, it -> addTillHigherThan(it, busIDList[index], earliestLeaveTs) }
    val firstBusList = afterStartPoint.mapIndexed { i, it -> Pair<Int, Int>(i, it - earliestLeaveTs) }
    var min = Pair<Int, Int>(Int.MAX_VALUE, Int.MAX_VALUE)
    for (fb in firstBusList) {
        if (fb.second < min.second) {
            min = fb
        }
    }
    return busIDList[min.first] * min.second
}

fun addTillHigherThan(v: Int, add: Int, max: Int): Int {
    var v1 = v
    while (v1 < max) {
        v1 += add
    }
    return v1
}
