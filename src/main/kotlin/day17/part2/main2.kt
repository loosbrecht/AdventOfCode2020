package main.kotlin.day17.part2

import main.kotlin.util.readInputForDay
import main.kotlin.util.readSmallInputForDay

fun main() {
    val lines = readInputForDay(17)

    val fourD = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Char>>>>()
    val twoD = mutableMapOf<Int, MutableMap<Int, Char>>()

    for ((j, l) in lines.withIndex()) {
        val m = mutableMapOf<Int, Char>()
        for ((i, c) in l.withIndex()) {
            m[i] = c
        }
        twoD[j] = m
    }
    val threeD = mutableMapOf(Pair(0, twoD))
    fourD[0] = threeD
    printCube(fourD)
    runStartUpCycle(fourD)
}

fun runStartUpCycle(fourD: MutableMap<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Char>>>>) {
    var currentSpace = fourD
    for (o in 1..6) {
        currentSpace = add1Neighbour(currentSpace)
        val nextSpace = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Char>>>>()
        for ((w, wSlice) in currentSpace) {
            val threeD = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, Char>>>()
            for ((z, zSlice) in wSlice) {
                val twoD = mutableMapOf<Int, MutableMap<Int, Char>>()
                for ((y, ySlice) in zSlice) {
                    val m = mutableMapOf<Int, Char>()
                    for ((x, xSlice) in ySlice) {
                        val neighbours = getAllNeighbours(w, z, y, x, currentSpace)
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
                threeD[z] = twoD
            }//zslice
            nextSpace[w] = threeD
        }//wslice
        currentSpace = nextSpace
        println("After $o cycles: ")
        printCube(currentSpace)
        println("Active cubes ${countActiveCubes(currentSpace)}")
    }
}

fun countActiveCubes(currentSpace: MutableMap<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Char>>>>): Int {
    var count = 0
    for ((l, wslice) in currentSpace) {
        for ((k, zslice) in wslice) {
            for ((j, yslice) in zslice) {
                for ((i, xslice) in yslice) {
                    if (xslice == '#') {
                        count++
                    }
                }
            }
        }
    }
    return count
}


fun printCube(currentSpace: MutableMap<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Char>>>>) {
    for ((l, wslice) in currentSpace) {
        for ((k, zslice) in wslice) {
            println("z=$l, w=$k")
            for ((j, yslice) in zslice) {
                for ((i, zslice) in yslice) {
                    print(zslice)
                }
                println()
            }
            println()
        }
        println()
    }
}

fun add1Neighbour(currentSpace: MutableMap<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Char>>>>): MutableMap<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Char>>>> {

    val wMin = currentSpace.keys.minOrNull()!!
    val wMax = currentSpace.keys.maxOrNull()!!
    val zMin = currentSpace[0]?.keys?.minOrNull()!!
    val zMax = currentSpace[0]?.keys?.maxOrNull()!!
    val yMin = currentSpace[0]?.get(0)?.keys?.minOrNull()!!
    val yMax = currentSpace[0]?.get(0)?.keys?.maxOrNull()!!
    val xMin = currentSpace[0]?.get(0)?.get(0)?.keys?.minOrNull()!!
    val xMax = currentSpace[0]?.get(0)?.get(0)?.keys?.maxOrNull()!!

    val fourD = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Char>>>>()
    for (l in wMin - 1..wMax + 1) {
        val threeD = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, Char>>>()
        for (k in zMin - 1..zMax + 1) {
            val twoD = mutableMapOf<Int, MutableMap<Int, Char>>()
            for (j in yMin - 1..yMax + 1) {
                val m = mutableMapOf<Int, Char>()
                for (i in xMin - 1..xMax + 1) {
                    if (currentSpace.containsKey(l)) {
                        if (currentSpace[l]?.containsKey(k)!!) {
                            if (currentSpace[l]?.get(k)?.containsKey(j)!!) {
                                if (currentSpace[l]?.get(k)?.get(j)?.containsKey(i)!!) {
                                    val currVal = currentSpace[l]?.get(k)?.get(j)?.get(i)!!
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
                    } else {
                        m[i] = '.'
                    }
                }//x
                twoD[j] = m
            }//y
            threeD[k] = twoD
        }//z
        fourD[l] = threeD
    }//w
    return fourD
}

fun getAllNeighbours(
    w: Int,
    z: Int,
    y: Int,
    x: Int,
    currentSpace: MutableMap<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Char>>>>
): List<Char> {
    val neighbours = mutableListOf<Char>()
    for (l in w - 1..w + 1) {
        for (k in z - 1..z + 1) {
            for (j in y - 1..y + 1) {
                for (i in x - 1..x + 1) {
                    if (currentSpace.containsKey(l)) {
                        if (currentSpace[l]?.containsKey(k)!!) {
                            if (currentSpace[l]?.get(k)?.containsKey(j)!!) {
                                if (currentSpace[l]?.get(k)?.get(j)?.containsKey(i)!!) {
                                    currentSpace[l]?.get(k)?.get(j)?.get(i)?.let { neighbours.add(it) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    //remove the element itself
    val el = currentSpace[w]?.get(z)?.get(y)?.get(x)
    neighbours.remove(el)

    return neighbours
}
