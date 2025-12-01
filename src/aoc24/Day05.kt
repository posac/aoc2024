import java.util.Collections

private const val DAY_NAME = "2024/Day05"
fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    val (orderRules, manuals) = parseGame(input)
    return manuals.filter {
        val sorted = sortByComparator(it, orderRules)
        it.println("Original")
        sorted == it
    }.sumOf { it[it.size/2].println("Middle point") }.toLong()
}

private fun sortByComparator(
    it: List<Int>,
    orderRules: Map<Position, Int>
): MutableList<Int> {
    val sorted = it.toMutableList()
    Collections.sort(sorted) { a, b ->
        val position = Position(a, b)
        if (orderRules.containsKey(position)) {
            orderRules[position]!!
        } else {
            b - a
        }

    }
    sorted.println("Sorted")
    return sorted
}

private fun parseGame(input: List<String>): Pair<Map<Position, Int>, List<List<Int>>> {
    val orderRules = input.takeWhile { it.isNotEmpty() }.flatMap {
        val (first, second) = it.split("|")
        val position = Position(first.toInt(), second.toInt())
        listOf(position to -1, position.transpose() to 1)
    }.toMap().println()
    val manuals = input.dropWhile { it.isNotEmpty() }.drop(1).map {
        it.split(",").map { it.toInt() }
    }
    return Pair(orderRules, manuals)
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 143L)
    check(part1(readInputResources(DAY_NAME, "input")).println("Part one test result") == 6041L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 123L)
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 4884L)
}

private fun part2(input: List<String>): Long {
    val (orderRules, manuals) = parseGame(input)
    return manuals.map {
        val sorted = sortByComparator(it, orderRules)
        it.println("Original")
        it to sorted
    }.filter { it.first != it.second }.sumOf { it.second[it.second.size/2].println("Middle point") }.toLong()
}


