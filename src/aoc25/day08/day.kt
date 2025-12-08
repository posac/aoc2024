package aoc25.day08

import combination
import println
import readInputResources
import kotlin.math.sqrt

private const val DAY_NAME = "2025/Day08"


fun main() {
    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}

data class Coordinates(val x: Int, val y: Int, val z: Int) {
    var coordinationGroup: CoordinationGroup = CoordinationGroup(mutableListOf(this))
    fun distanceTo(other: Coordinates) =
        sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z) * 1.0)
}

data class CoordinationGroup(val coordinates: MutableList<Coordinates>) {
    init {
        coordinates.forEach {
            it.coordinationGroup = this
        }
    }


    fun add(coordinateGroup: CoordinationGroup) {
        coordinateGroup.coordinates.forEach { coordinate ->
            coordinates.add(coordinate)
            coordinate.coordinationGroup = this
        }

    }

}

private fun part1(input: List<String>): Long {
    val coordinates =
        input.map { it.split(",").map { it.toInt() } }.map { Coordinates(x = it[0], y = it[1], z = it[2]) }
    val iterations = 10

    var workingCoordinates = coordinates.toMutableList().combination()

    val processed = mutableListOf<Coordinates>()
    repeat(iterations) {
        it.println("Iteration:")
        val pairToProcess = workingCoordinates.minBy {
            val result = when {
                it.first.coordinationGroup != it.second.coordinationGroup -> it.first.distanceTo(it.second)
                    .toInt()

                else -> Int.MAX_VALUE
            }
            result
        }.also {
            processed.add(it.first)
            processed.add(it.second)
        }
        pairToProcess.println("Pair to process:")

        val group = pairToProcess.first.coordinationGroup

        group.add(pairToProcess.second.coordinationGroup)
        group.coordinates.size.println("Merged group size:")

        coordinates.map { it.coordinationGroup }.distinct().sortedByDescending { it.coordinates.size }
            .println("Groups:")
    }
    val groups = coordinates.map { it.coordinationGroup }.distinct()
    processed.sortedBy{ it.x }.forEach {
        it.println("Processed:")
    }

    coordinates.forEach { cord ->
        println("coordinate $cord was processed in ${processed.count { it == cord }} ")
    }
    return groups.map { it.coordinates.size.toLong() }.sortedDescending().take(3).println("Top 3:")
        .reduce { acc, l -> acc * l }
}


private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 40L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 281L)
}

private fun part2(input: List<String>): Long = input.size.toLong()


