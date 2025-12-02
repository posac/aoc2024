package aoc25.day02

import println
import readInputResources
import kotlin.math.pow

private const val DAY_NAME = "2025/Day02"

data class Ranges(val first: Long, val last: Long)

fun main() {
    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    val game = input.flatMap { it.split(",") }.filter { it.isNotBlank() }
        .map { val range = it.split("-").map { it.toLong() }; Ranges(range[0], range[1]) }
    game.forEach { println(it) }

    return game.flatMap {
        lookForInvalid(it).println("Result for $it is : ")

    }.sum()

}

private fun lookForInvalid(ranges: Ranges): List<Long> {
    val sizeOfStart = ranges.first.toString().length
    val sizeOfEnd = ranges.last.toString().length
    return when {
        (sizeOfStart == sizeOfEnd && sizeOfStart % 2 == 1) -> emptyList<Long>()
        (sizeOfStart == sizeOfEnd && sizeOfStart % 2 == 0) -> {
            val places = sizeOfStart / 2
            val start = ranges.first.toString().substring(startIndex = 0, endIndex = places).toLong()
            val end = ranges.last.toString().substring(startIndex = 0, endIndex = places).toLong()
            LongRange(start, end)
                .map { (it * 10.0.pow(places).toLong() + it)}
                .filter { it <= ranges.last && it >= ranges.first}
        }

        else -> {
            val split = (ranges.first + ranges.last) / 2
            lookForInvalid(Ranges(ranges.first, split)) + lookForInvalid(Ranges(split + 1, ranges.last))
        }
    }


}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 1227775554L)
    check(part1(readInputResources(DAY_NAME, "input")).println("Part one test result") == 31000881061L)

}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 281L)
}

private fun part2(input: List<String>): Long = input.size.toLong()


