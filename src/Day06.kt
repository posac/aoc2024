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
    check(part2(readInputResources(DAY_NAME, "input")).println("Part two test result") < 1785L)
}

private fun part2(input: List<String>): Long {
    val triple = parseGame(input)
    val game = triple.first
    val startingPoint = triple.second
    var direction = triple.third

    return checkNoRec(startingPoint,  game, direction)

}


data class Movement(
    val currentPosition: Position,
    val direction: Direction
)

data class PathItem(
    val currentPosition: Position,
    val direction: Direction,
    val pathId:String,
    val newObsticle: Position? = null
){
    fun turnLeft() = copy(direction = direction.turnLeft())

    fun moveForward() = copy(currentPosition = currentPosition.move(direction, 1))

    fun toMovement() = Movement(currentPosition, direction)
}

private fun checkNoRec(
    currentPosition: Position,
    game: Map<Position, Char>,
    direction: Direction
): Long {
    val positionsToCheck = mutableListOf(PathItem(currentPosition,  direction, "starting/") )
    val paths = mutableMapOf<String, MutableSet<Movement>>()
    val cache = mutableMapOf<Movement, Int>()
    val newObsticle = mutableSetOf<Position>()
    while (positionsToCheck.isNotEmpty()) {
        val pathItem = positionsToCheck.removeAt(0)
        val (currentPosition, direction, pathId) = pathItem
        val nextPosition = currentPosition.move(direction, 1)

        when{
            !game.containsKey(currentPosition) ->{}
            paths.computeIfAbsent(pathItem.pathId){ mutableSetOf()}.contains(pathItem.toMovement()) -> {
                if(pathItem.newObsticle!=null)
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




    return newObsticle.size.toLong()


}

private fun finishGame(
    currentPosition: Position,
    visitedPositions: Set<Pair<Position, Direction>>,
    game: Map<Position, Char>,
    direction: Direction
): Long {
    var direction1 = direction
    lateinit var nextPosition: Position
    var curr = currentPosition
    val visitedPositions = visitedPositions.toMutableSet()
    while (game.containsKey(curr)) {
        nextPosition = currentPosition.move(direction1, 1)
        if (visitedPositions.contains(curr to direction1)) {
            return 1
        }
        if (game[nextPosition] == '#') {
            direction1 = direction1.turnLeft()
        }

        visitedPositions.add(currentPosition to direction1)
        curr = curr.move(direction1, 1)

    }
    return 0
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
        '^' -> Direction.NORT
        'v' -> Direction.SOUTH
        '<' -> Direction.WEST
        '>' -> Direction.EAST
        else -> throw IllegalArgumentException("Invalid starting point")
    }.println("Direction")
    return Triple(game, startingPoint, direction)
}


