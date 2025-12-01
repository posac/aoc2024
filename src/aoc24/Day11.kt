private const val DAY_NAME = "2024/Day11"
fun main() {
    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part1(input, 75).println("Part two result:")
}


private fun part1(input: List<String>, blinks: Int = 25): Long {
    val game = input.first().split(" ").map { it.toLong() }

    return simulateBlinking(game, blinks)
}


fun simulateBlinking(stones: List<Long>, blinks: Int): Long {
    blinks.println("Processing ${stones.size}")


    var result = stones.sumOf { stone ->
        stoneCount(stone, blinks)
    }
    return result
}

val cache = mutableMapOf<Pair<Long, Int>, Long>()
fun stoneCount(stone: Long, blinks: Int): Long {
    if (blinks == 0)
        return 1
    if (stone to blinks in cache) return cache[stone to blinks]!!

    val stringRepresentation = stone.toString()
    val length = stringRepresentation.length

    val result = when {
        stone == 0L -> stoneCount(1L, blinks - 1)
        (length.mod(2) == 0) -> stoneCount(
            stringRepresentation.substring(0, length / 2).toLong(),
            blinks - 1
        ) + stoneCount(stringRepresentation.substring(length / 2).toLong(), blinks - 1)

        else -> stoneCount(stone * 2024, blinks - 1)
    }

    cache[stone to blinks] = result
    return result
}


private fun checkPart1() {


    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 55312L)
    check(part1(readInputResources(DAY_NAME, "input")).println("Part one test result") == 211306L)


}



