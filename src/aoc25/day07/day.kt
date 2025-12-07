package aoc25.day07

import println
import readInputResources

private const val DAY_NAME = "2025/Day07"


fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    val beams = setOf(input.first().indexOf('S'))
    val splitters = mutableListOf<Pair<Int, Int>>()

    input.withIndex().drop(1).fold(beams) { acc, row ->
        acc.flatMap {
            val beamChar = row.value[it]
            when (beamChar) {
                '^' -> {
                    splitters.add(it to row.index)
                    splitters.println("Splitting ")
                    listOf(it - 1, it + 1)
                }

                else -> listOf(it)
            }

        }.toSet()
    }

    return splitters.count().toLong()
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 21L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 40L)
}


private fun part2(input: List<String>): Long {
    return process(input, input.first().indexOf('S'))
}

val cache = mutableMapOf<Pair<Int, Int>, Long>()
private fun process(
    input: List<String>,
    beam: Int,
    index: Int = 1,
): Long {
    val cached = cache.get(index to beam)
    if (cached != null) return cached


    val result =    when {
            index >= input.size   -> 1L
            input[index][beam] == '^' -> process(input, beam - 1, index + 1) + process(input, beam + 1, index + 1)
            else -> process(input, beam, index + 1)
        }

    return result.also { cache[index to beam] = it }
}


