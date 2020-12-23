package main.kotlin.day23


fun main() {

    //actual input is 712643589
    //example input is 389125467
    var inputList = "389125467".split("").filter { it != "" }.map(String::toInt).toList()
    val clockPart1 = findNewOrder(inputList)
    println(clockPart1.getCorrectOrderForResult())

    inputList = extendInputList(inputList)
    val starOneLbl = inputList[1]
    val starTwoLbl = inputList[2]
    val clockPart2 = findNewOrder(inputList, 10_000_000)
    val starOneIdx = clockPart2.getIndexForLabel(starOneLbl)
    val starTwoIdx = clockPart2.getIndexForLabel(starTwoLbl)
    println("$starOneIdx $starTwoIdx ${starOneIdx.toLong() * starTwoIdx.toLong()}")


}

fun extendInputList(inputList: List<Int>): List<Int> {
    val extended = (10..1_000_000).toList()
    return inputList + extended
}

fun findNewOrder(inputList: List<Int>, moves: Int = 100): Clock {
    val clock = Clock(inputList.toMutableList())
    for (i in 1..moves) {
        println(i)
        val threeAfterCurrent = clock.removeAndGetThree()
        val destination = clock.getDestinationIdx(threeAfterCurrent)
        val destIdx = clock.getIndexForLabel(destination)
        clock.addElementsAtIdx(destIdx, threeAfterCurrent)
        clock.nextCurrent()

    }
    return clock
}

class Clock(var clockElements: MutableList<Int>) {
    var clockPosition: Int
    var clockLabel: Int
    val maxVal: Int
    val minVal: Int

    init {
        clockPosition = 0
        clockLabel = clockElements[0]
        maxVal = clockElements.maxOrNull()!!
        minVal = clockElements.minOrNull()!!
    }

    fun removeAndGetThree(): List<Int> {
        val removedElements = mutableListOf<Int>()
        for (i in 1..3) {
            var removeIdx = clockPosition + 1
            if (removeIdx >= clockElements.size) {
                removeIdx = 0
            }
            removedElements.add(clockElements.removeAt(removeIdx))
        }
        return removedElements
    }

    fun addElementsAtIdx(idx: Int, elements: List<Int>) {
        val afterIdx = idx + 1
        if (afterIdx >= clockElements.size) {
            clockElements.addAll(elements)
        } else {
            clockElements.addAll(afterIdx, elements)
        }
    }

    fun getDestinationIdx(threeAfterCurrent: List<Int>): Int {
        var destination = clockLabel - 1
        if (destination < minVal) {
            destination = maxVal
        }
        while (threeAfterCurrent.contains(destination)) {
            destination--
            if (destination < minVal) {
                destination = maxVal
            }
        }
        return destination
    }

    fun nextCurrent() {
        clockPosition = getIndexForLabel(clockLabel)
        clockPosition++
        if (clockPosition >= clockElements.size) {
            clockPosition = 0
        }
        clockLabel = clockElements[clockPosition]
    }

    override fun toString(): String {
        return clockElements.toString() + " CurPos: " + clockPosition + " CurLbl " + clockLabel
    }

    fun getIndexForLabel(lbl: Int): Int {
        for ((i, el) in clockElements.withIndex()) {
            if (el == lbl) {
                return i
            }
        }
        return 0
    }

    fun getCorrectOrderForResult(): String {
        val idx = getIndexForLabel(1)
        val res = clockElements.subList(idx + 1, clockElements.size).joinToString(separator = "") { it.toString() }
        return res + clockElements.subList(0, idx).joinToString(separator = "") { it.toString() }

    }

}


