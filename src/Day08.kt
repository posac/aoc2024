private const val DAY_NAME = "Day08"
fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    val game = parsePositionGame(input) { it }

    return processFrequency(game)

}

private fun processFrequency(game: Map<Position, Char>, firstOnly: Boolean = true, addStations: Boolean = false): Long {
    val frequencyGroups = game.entries.groupBy { it.value }

    val antinodes = frequencyGroups.flatMap {
        if (it.key == '.') emptyList() else {
            val antinodes = it.value.map { it.key }.combination().flatMap {
                val rowDiff = it.first.row - it.second.row
                val columnDiff = it.first.column - it.second.column

                calculateAntinodes(it.first, -rowDiff, -columnDiff, game, firstOnly) + calculateAntinodes(
                    it.second,
                    rowDiff,
                    columnDiff,
                    game,
                    firstOnly
                )
            }
            antinodes
        }.filter { it in game.keys }
    } + (if (addStations) game.keys.filter { game[it] != '.' } else emptyList())



    return antinodes.toSet().size.toLong()
}

private fun calculateAntinodes(
    position: Position,
    rowDiff: Long,
    columnDiff: Long,
    game: Map<Position, Char>,
    firstOnly: Boolean
): List<Position> {
    val result = mutableListOf<Position>()
    var currentPosition = position
    while (currentPosition in game.keys) {
        currentPosition = currentPosition.move(Direction.NORTH, rowDiff).move(Direction.WEST, columnDiff)
        result.add(currentPosition)
        if (firstOnly) break
    }

    return result

}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 14L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 34L)
}

private fun part2(input: List<String>): Long {
    val game = parsePositionGame(input) { it }
    return processFrequency(game, firstOnly = false, addStations = true)

}