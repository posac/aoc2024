import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

private const val DAY_NAME = "Day06"
fun main() {
//    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    val triple = parseGame(input)
    val game = triple.first
    val startingPoint = triple.second
    var direction = triple.third


    val visitedPositions = mutableSetOf<Position>()
    lateinit var nextPosition: Position
    var currentPosition = startingPoint
    while (game.containsKey(currentPosition)) {
        nextPosition = currentPosition.move(direction, 1)
        if (game[nextPosition] == '#') {
            direction = direction.turnLeft()

        }
        visitedPositions.add(currentPosition)
        currentPosition = currentPosition.move(direction, 1)

    }

    return visitedPositions.size.toLong()
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 41L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test_part_2_unit")).println("Part two test result") == 1L)
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 6L)
    val part2 = part2(readInputResources(DAY_NAME, "input"))
    check(part2.println("Part two test result") < 1785L)
    check(part2.println("Part two test result") != 1784L)
    check(part2.println("Part two test result") != 1606L)
    check(part2.println("Part two test result") != 1663L)
}

private fun part2(input: List<String>): Long {
    val triple = parseGame(input)
    val game = triple.first
    val startingPoint = triple.second
    var direction = triple.third

//    val newObs = checkNoRec(startingPoint, game, direction)
//
//    File("test_input_result").writeText(
//        input.mapIndexed { row, s ->
//            s.mapIndexed { col, c -> if (newObs.contains(Position(row, col))) 'O' else c }.joinToString("")
//        }.joinToString("\n")
//    )
    return checkBrutForce(startingPoint, game, direction)

}


data class Movement(
    val currentPosition: Position,
    val direction: Direction
)

data class PathItem(
    val currentPosition: Position,
    val direction: Direction,
    val pathId: String,
    val newObsticle: Position? = null
) {
    fun turnLeft() = copy(direction = direction.turnLeft())

    fun moveForward() = copy(currentPosition = currentPosition.move(direction, 1))

    fun toMovement() = Movement(currentPosition, direction)
}


private fun checkBrutForce(
    startingPosition: Position,
    game: Map<Position, Char>,
    startingDirection: Direction
): Long {
    return runBlocking(Dispatchers.Default) {
        async {
            game.filter { it.value != '#' }.keys.count {
                val newGame = game.toMutableMap()
                newGame[it] = '#'
                checkLoop(startingPosition, newGame, startingDirection)
            }
        }.await()
    }.toLong()
}

private fun checkNoRec(
    startingPosition: Position,
    game: Map<Position, Char>,
    startingDirection: Direction
): Set<Position> {
    val positionsToCheck = mutableListOf(PathItem(startingPosition, startingDirection, "starting/"))
    val paths = mutableMapOf<String, MutableSet<Movement>>()
    val newObsticle = mutableSetOf<Position>()
    while (positionsToCheck.isNotEmpty()) {
        val pathItem = positionsToCheck.removeAt(0)
        val (currentPosition, direction, pathId) = pathItem
        val nextPosition = currentPosition.move(direction, 1)

        when {
            !game.containsKey(nextPosition) -> {}
            paths.computeIfAbsent(pathItem.pathId) { mutableSetOf() }.contains(pathItem.toMovement()) -> {
                if (pathItem.newObsticle != null)
                    newObsticle.add(pathItem.newObsticle)
            }

            game[nextPosition] == '#' -> positionsToCheck.add(pathItem.turnLeft())
            pathItem.newObsticle == null -> {
                positionsToCheck.add(pathItem.moveForward())
                val newPathId = pathItem.pathId + "${nextPosition}"
                positionsToCheck.add(pathItem.turnLeft().copy(newObsticle = nextPosition, pathId = newPathId))
                paths[newPathId] = paths[pathId]!!.toMutableSet()
            }

            else -> positionsToCheck.add(pathItem.moveForward())
        }
        paths[pathId]!!.add(pathItem.toMovement())

    }


    return newObsticle.filter {
        val newGame = game.toMutableMap()
        newGame[it] = '#'
        checkLoop(startingPosition, newGame, startingDirection)
    }.toSet()


}

private fun checkLoop(
    currentPosition: Position,
    game: Map<Position, Char>,
    direction: Direction
): Boolean {
    var direction1 = direction
    lateinit var nextPosition: Position
    var curr = currentPosition
    val visitedPositions = mutableSetOf<Pair<Position, Direction>>()
    while (game.containsKey(curr)) {
        nextPosition = curr.move(direction1, 1)
        if (visitedPositions.contains(curr to direction1)) {
            return true
        }
        if (game[nextPosition] == '#') {
            direction1 = direction1.turnLeft()
            continue
        }

        visitedPositions.add(curr to direction1)
        curr = curr.move(direction1, 1)

    }
    return false
}

private fun parseGame(input: List<String>): Triple<Map<Position, Char>, Position, Direction> {
    val game = input.flatMapIndexed { rowIndex: Int, row: String ->
        row.mapIndexed { colIndex, char ->
            Position(
                row = rowIndex,
                column = colIndex
            ) to char
        }
    }.toMap()


    val startingPoint = game.filter { !setOf('.', '#').contains(it.value) }.let {
        it.println()
        require(it.size == 1)
        it.keys.first()

    }.println("Starting point")


    var direction = when (game[startingPoint]) {
        '^' -> Direction.NORTH
        'v' -> Direction.SOUTH
        '<' -> Direction.WEST
        '>' -> Direction.EAST
        else -> throw IllegalArgumentException("Invalid starting point")
    }.println("Direction")
    return Triple(game, startingPoint, direction)
}


