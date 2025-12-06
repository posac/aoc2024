package aoc25.day06

import println
import readInputResources

private const val DAY_NAME = "2025/Day06"



fun main() {
//    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}

data class Group(
    val operator : Char ,
    val numbers : List<Long>
)


private fun part1(input: List<String>): Long {


    val lanes = input.map{
        it.split("\\s+".toRegex()).filter { it.isNotBlank() }
    }


    val groups = lanes.last().withIndex().map { group ->
        Group(operator = group.value.first(),

            numbers= lanes.dropLast(1).map { it[group.index].toLong() }

        )
    }



    groups.forEach { println(it) }

    return groups.sumOf { group ->
        when(group.operator) {
            '+' -> group.numbers.sum()
            '*' -> group.numbers.reduce { acc, l -> acc * l }
            else -> 0L
        }
    }
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 4277556L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 3263827L)
}

private fun part2(input: List<String>): Long {

    val dropLast = input.dropLast(1)
    val lanes = dropLast.map{
        it.split("\\s+".toRegex()).filter { it.isNotBlank() }
    }


    val operator = input.last().withIndex().filter { it.value in listOf('+', '*') }.map { it.index to it.value }.toMap()
    val last = operator.entries.sortedBy { it.key }.last()
    val groupBlockSizes = operator.entries.sortedBy { it.key }.zipWithNext().map { it.first.key to it.second.key - it.first.key - 1 }.toMap() +
            mapOf(last.key  to input.first().length - last.key )

    println(operator)
    println(groupBlockSizes)
    val groups = operator.map { (index, operator) ->
        val groupSize = groupBlockSizes[index]!!
        Group(operator = operator,
            numbers = (1..groupSize).map { digit ->
                dropLast.map { val processingIndex = index + digit - 1
                    processingIndex.println("$it $index $digit")
                    it[processingIndex]}.joinToString("").trim().toLong()
            }
            )
    }



    groups.forEach { println(it) }

    return groups.sumOf { group ->
        when(group.operator) {
            '+' -> group.numbers.sum()
            '*' -> group.numbers.reduce { acc, l -> acc * l }
            else -> 0L
        }
    }
}


