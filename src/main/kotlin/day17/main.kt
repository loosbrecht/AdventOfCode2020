package main.kotlin.day17

import main.kotlin.util.readSmallInputForDay

fun main() {
    val threeD = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, Char>>>()
    val lines = readSmallInputForDay(17)
    val twoD = mutableMapOf<Int, MutableMap<Int, Char>>()
    for ((j, l) in lines.withIndex()) {
        val m = mutableMapOf<Int, Char>()
        for ((i, c) in l.withIndex()) {
            m[i] = c
        }
        twoD[j] = m
    }
    threeD[0] = twoD
    printCube(threeD)
    runStartUpCycle(threeD)
}

fun runStartUpCycle(threeD: Map<Int, Map<Int, Map<Int, Char>>>): Map<Int, Map<Int, Map<Int, Char>>> {
    var currentSpace = threeD
    for (o in 1..6) {
        currentSpace = add1Neighbour(currentSpace)
        val nextSpace = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, Char>>>()
        for ((z, zSlice) in currentSpace) {
            val twoD = mutableMapOf<Int, MutableMap<Int, Char>>()
            for ((y, ySlice) in zSlice) {
                val m = mutableMapOf<Int, Char>()
                for ((x, xSlice) in ySlice) {
                    val neighbours = getAllNeighbours(z, y, x, currentSpace)
                    when (xSlice) {
                        '.' -> {
                            when (neighbours.count { it == '#' }) {
                                3 -> m[x] = '#'
                                else -> m[x] = '.'
                            }
                        }
                        '#' -> {
                            when (neighbours.count { it == '#' }) {
                                2, 3 -> m[x] = '#'
                                else -> m[x] = '.'
                            }
                        }
                    }
                }//xslice
                twoD[y] = m
            }//yslice
            nextSpace[z] = twoD
        }//zslice
        currentSpace = nextSpace
        println("After $o cycles: ")
        printCube(currentSpace)
        println("Active cubes ${countActiveCubes(currentSpace)}")
    }
    return currentSpace
}

fun countActiveCubes(currentSpace: MutableMap<Int, MutableMap<Int, MutableMap<Int, Char>>>): Int {
    var count = 0
    for ((k, zslice) in currentSpace) {
        for ((j, yslice) in zslice) {
            for ((i, xslice) in yslice) {
                if (xslice == '#') {
                    count++
                }
            }
        }
    }
    return count
}


fun printCube(currentSpace: Map<Int, Map<Int, Map<Int, Char>>>) {
    for ((k, zslice) in currentSpace) {
        for ((j, yslice) in zslice) {
            for ((i, zslice) in yslice) {
                print(zslice)
            }
            println()
        }
        println()
    }
}

fun add1Neighbour(currentSpace: Map<Int, Map<Int, Map<Int, Char>>>): Map<Int, Map<Int, Map<Int, Char>>> {
    val zMin = currentSpace.keys.minOrNull()!!
    val zMax = currentSpace.keys.maxOrNull()!!
    val yMin = currentSpace[0]?.keys?.minOrNull()!!
    val yMax = currentSpace[0]?.keys?.maxOrNull()!!
    val xMin = currentSpace[0]?.get(0)?.keys?.minOrNull()!!
    val xMax = currentSpace[0]?.get(0)?.keys?.maxOrNull()!!

    val threeD = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, Char>>>()
    for (k in zMin - 1..zMax + 1) {
        val twoD = mutableMapOf<Int, MutableMap<Int, Char>>()
        for (j in yMin - 1..yMax + 1) {
            val m = mutableMapOf<Int, Char>()
            for (i in xMin - 1..xMax + 1) {
                if (currentSpace.containsKey(k)) {
                    if (currentSpace[k]?.containsKey(j)!!) {
                        if (currentSpace[k]?.get(j)?.containsKey(i)!!) {
                            val currVal = currentSpace[k]?.get(j)?.get(i)!!
                            m[i] = currVal
                        } else {
                            m[i] = '.'
                        }
                    } else {
                        m[i] = '.'
                    }
                } else {
                    m[i] = '.'
                }
            }//x
            twoD[j] = m
        }//y
        threeD[k] = twoD
    }//z
    return threeD
}

fun getAllNeighbours(z: Int, y: Int, x: Int, currentSpace: Map<Int, Map<Int, Map<Int, Char>>>): List<Char> {
    val neighbours = mutableListOf<Char>()

    for (k in z - 1..z + 1) {
        for (j in y - 1..y + 1) {
            for (i in x - 1..x + 1) {
                if (currentSpace.containsKey(k)) {
                    if (currentSpace[k]?.containsKey(j)!!) {
                        if (currentSpace[k]?.get(j)?.containsKey(i)!!) {
                            currentSpace[k]?.get(j)?.get(i)?.let { neighbours.add(it) }
                        }
                    }
                }
            }
        }
    }
    //remove the element itself
    val el = currentSpace[z]?.get(y)?.get(x)
    neighbours.remove(el)

    return neighbours
}
