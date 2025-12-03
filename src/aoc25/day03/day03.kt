package aoc25.day03

import println
import readInputResources

private const val DAY_NAME = "2025/Day03"


fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    return input.map {
        it.map { it.digitToInt() }
    }.map {
        val first = it.dropLast(1).max()
        val idx = it.indexOfFirst { it == first }
        val last = it.drop(idx+1).max()
        (first * 10 + last)
    }.sum()
        .toLong()
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 357L)
    check(part1(readInputResources(DAY_NAME, "input")).println("Part one test result") == 17244L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 3121910778619L)
    check(part2(readInputResources(DAY_NAME, "input")).println("Part two test result") == 171435596092638L)
}

private fun part2(input: List<String>): Long {
    return input.map {
        it.map { it.digitToInt() }
    }.map {
        findNumbers(it, 12).joinToString("").toLong()
    }.sum()
}

fun findNumbers(it: List<Int>, number: Int) : List<Int> {
    if(number ==0 ) return emptyList()
    val first = it.dropLast(number-1).max()
    val idx = it.indexOfFirst { it == first }
    return listOf(first) + findNumbers(it.drop(idx+1), number-1)
}


