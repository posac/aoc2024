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
        countPaths(it.key, game)
    }.flatten().size.toLong()
}

private fun parseGame(input: List<String>): Pair<Map<Position, Int?>, Map<Position, Int?>> {
    val game = input.mapIndexed { rowIdx, row ->
        row.mapIndexed { columnIdx, column -> Position(rowIdx, columnIdx) to column.digitToIntOrNull() }
    }.flatten().toMap().println("Game")

    val findBegginings = game.filter { it.value == 0 }
    findBegginings.println("Starting Points")
    return Pair(game, findBegginings)
}


fun countPaths(beggining: Position, game: Map<Position, Int?>): Set<Position> {
    val toVisit = mutableListOf(beggining)
    val visited = mutableSetOf<Position>()
    val picks = mutableSetOf<Position>()

    while (toVisit.isNotEmpty()) {
        val current = toVisit.removeFirst().println("Current")
        if (current in visited) continue
        visited.add(current)
        val value = game[current]
        if (value == null) continue
        if (value == 9) {
            "Found end".println("[$beggining] $current")
            picks.add(current)
            continue
        }

        addToVisitNextStep(current, game, toVisit, Direction.NORT)
        addToVisitNextStep(current, game, toVisit, Direction.SOUTH)
        addToVisitNextStep(current, game, toVisit, Direction.WEST)
        addToVisitNextStep(current, game, toVisit, Direction.EAST)

    }
    return picks
}

private fun addToVisitNextStep(
    current: Position,
    game: Map<Position, Int?>,
    toVisit: MutableList<Position>,
    direction: Direction
) {
    val next = current.move(direction)
    val nextValue = game[next].println("[$next] nextValue")
    val currentValue = game[current].println("[$next] CurrentValue")
    if (nextValue == null || currentValue == null) return
    if (next in game && nextValue - currentValue == 1) toVisit.add(next)
}

private fun checkPart1() {
//    check(part1(readInputResources(DAY_NAME, "test_01")).println("Part one test result") == 2L)
//    check(part1(readInputResources(DAY_NAME, "test_02")).println("Part one test result") == 4L)
//    check(part1(readInputResources(DAY_NAME, "test_03")).println("Part one test result") == 2L)
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 36L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 81L)
}

private fun part2(input: List<String>): Long {
    val (game, findBegginings) = parseGame(input)



    return findBegginings.entries.sumOf {
        val toVisit = mutableListOf(it.key)
        var count = 0

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst().println("Current")
            val value = game[current]
            if (value == null) continue
            if (value == 9) {
                "Found end".println("[$it.key] $current")
                count++
                continue
            }

            addToVisitNextStep(current, game, toVisit, Direction.NORT)
            addToVisitNextStep(current, game, toVisit, Direction.SOUTH)
            addToVisitNextStep(current, game, toVisit, Direction.WEST)
            addToVisitNextStep(current, game, toVisit, Direction.EAST)

        }
        count
    }.toLong()




}


