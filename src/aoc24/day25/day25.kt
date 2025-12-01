package aoc25.day01

import println
import readInputResources

private const val DAY_NAME = "2024/Day25"


fun main() {
    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:").apply {
        check(this == 3356L)
    }
    part2(input).println("Part two result:")
}

data class Key(val heights: List<Int>)
data class Pins(val heights: List<Int>)

private fun part1(input: List<String>): Long {
    val keys = mutableListOf<Key>()
    val pins = mutableListOf<Pins>()
    input.chunked(8).forEach {
        val toProcess = it.filter { it.isNotEmpty() }
        println(toProcess)
        val isPin = toProcess.first().all { it == '#' }
        val heightsMap = it.flatMapIndexed { rowIndex: Int, it: String ->
            it.withIndex()
                .map { (index, c) ->
                    index to if (c == '#')
                        if (isPin) rowIndex else toProcess.size - rowIndex - 1
                    else
                        null
                }.filter { it.second != null }
        }
            .groupBy { it.first }.mapValues { it.value.mapNotNull { it.second }.max() }

        val heights = heightsMap.keys.sorted().map { heightsMap[it]!! }

        if (isPin)
            pins.add(Pins(heights))
        else
            keys.add(Key(heights))
    }

    keys.println("Keys")
    pins.println("Pins")

    return keys.flatMap { key ->
        pins.filter { pin ->
            val zipped = key.heights.zip(pin.heights)
            val resutl = zipped.all { it.first + it.second < 6 }
            println("$pin key=$key  $resutl")
            resutl
        }.map { key to it }
    }.size.toLong()
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 3L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 281L)
}

private fun part2(input: List<String>): Long = input.size.toLong()


