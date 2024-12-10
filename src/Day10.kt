private const val DAY_NAME = "Day10"
fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    val (game, findBegginings) = parseGame(input)

    return findBegginings.entries.map {
        countPaths(it.key, game).toSet()
    }.flatten().size.toLong()
}

private fun parseGame(input: List<String>): Pair<Map<Position, Int?>, Map<Position, Int?>> {
    val game = parsePositionGame(input) { it.digitToIntOrNull() }
    val findBegginings = game.filter { it.value == 0 }
    findBegginings.println("Starting Points")
    return Pair(game, findBegginings)
}


fun countPaths(beggining: Position, game: Map<Position, Int?>): List<Position> {
    val result = processItems(State(listOf(beggining))) { current, state ->
        val value = game[current]
        when {
            value == null -> {}
            value == 9 -> {
                "Found end".println("[$current.key] $current")
                state.acceptResult(current)
            }
            else -> {
                addToVisitNextStep(current, game, state, Direction.NORTH)
                addToVisitNextStep(current, game, state, Direction.SOUTH)
                addToVisitNextStep(current, game, state, Direction.WEST)
                addToVisitNextStep(current, game, state, Direction.EAST)
            }
        }
    }


    return result
}

private fun addToVisitNextStep(
    current: Position,
    game: Map<Position, Int?>,
    state: State<Position, Position>,
    direction: Direction
) {
    val next = current.move(direction)
    val nextValue = game[next].println("[$next] nextValue")
    val currentValue = game[current].println("[$next] CurrentValue")
    if (nextValue == null || currentValue == null) return
    if (next in game && nextValue - currentValue == 1) state.addToProcessing(next)
}

private fun checkPart1() {
//    check(part1(readInputResources(DAY_NAME, "test_01")).println("Part one test result") == 2L)
//    check(part1(readInputResources(DAY_NAME, "test_02")).println("Part one test result") == 4L)
//    check(part1(readInputResources(DAY_NAME, "test_03")).println("Part one test result") == 2L)
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 36L)
    check(part1(readInputResources(DAY_NAME, "input")).println("Part one test result") == 754L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 81L)
    check(part2(readInputResources(DAY_NAME, "input")).println("Part two test result") == 1609L)
}

private fun part2(input: List<String>): Long {
    val (game, findBegginings) = parseGame(input)
    return findBegginings.entries.map {
        countPaths(it.key, game)
    }.flatten().size.toLong()


}


