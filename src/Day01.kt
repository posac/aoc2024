import kotlin.math.abs

private const val DAY_NAME = "Day01"
fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    val (firstArray, secondArray) = parse(input)

    return firstArray.sorted().zip(secondArray.sorted()).sumOf { (first, second) ->
        abs(first - second)
    }

}

private fun parse(input: List<String>): Pair<MutableList<Long>, MutableList<Long>> {
    val firstArray = mutableListOf<Long>()
    val secondArray = mutableListOf<Long>()

    input.forEach {
        it.split("\\s+".toRegex()).map { it.toLong() }.apply { require(size == 2) }.let {
            firstArray.add(it[0])
            secondArray.add(it[1])
        }
    }
    return Pair(firstArray, secondArray)
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 11L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 31L)
}

private fun part2(input: List<String>): Long {
    val (firstArray, secondArray) = parse(input)
    val counts = secondArray.groupBy { it }.mapValues { it.value.size }
    return firstArray.sumOf {
        counts.getOrDefault(it, 0) * it
    }

}


