import kotlin.math.min

private const val DAY_NAME = "Day15"
fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
//    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}

sealed class Day15Object(val symbol : Char) {
    data object STARTING : Day15Object('@')
    data object WALL : Day15Object('#')
    data object EMPTY : Day15Object('.')
    data object BOX : Day15Object('O')
    data class BIGGER_BOX(var allPosition: List<Position>) : Day15Object('O')

    companion object {
        fun getValue(c: Char) = when (c) {
            '#' -> WALL
            'O' -> BOX
            '@' -> STARTING
            '.' -> EMPTY
            else -> throw IllegalArgumentException("Invalid value '$c'")
        }
    }
}

private fun part1(input: List<String>): Long {
    val game = parsePositionGame(input.takeWhile { it.isNotEmpty() }) { Day15Object.getValue(it) }.println("Game")

    val movement = input.dropWhile { it.isNotEmpty() }.flatMap {
        it.map {
            when (it) {
                '^' -> Direction.NORTH
                'v' -> Direction.SOUTH
                '<' -> Direction.WEST
                '>' -> Direction.EAST
                else -> throw IllegalArgumentException("Invalid value")
            }
        }
    }.toMutableList().println("Movement")

    val startingPoint = game.filterValues { it == Day15Object.STARTING }.keys.first().println("Starting point")


    val workingGame = game.toMutableMap()
    var currentPosition = startingPoint
    while (movement.isNotEmpty()) {
        val direction = movement.removeFirst()
        val nextPosition = currentPosition.move(direction)
        when {
            workingGame[nextPosition] == Day15Object.WALL -> {
                "Wall".println()
            }

            workingGame[nextPosition] == Day15Object.EMPTY -> {
                "Empty".println()
                currentPosition = moveRobot(workingGame, currentPosition, nextPosition)
            }

            workingGame[nextPosition] == Day15Object.BOX -> {
                val (canMove, boxesToMove) = canMoveBox(nextPosition, direction, workingGame)
                boxesToMove.println("canMove: $canMove")
                if (canMove) {
                    val firstBox = boxesToMove.first()
                    val placeToMove = boxesToMove.last().move(direction)


                    moveBox(workingGame, firstBox, placeToMove)

                    currentPosition = moveRobot(workingGame, currentPosition, nextPosition)
                }

            }

            else -> throw IllegalArgumentException("Invalid value")

        }
    }

    workingGame.println("After game")
    return workingGame.filter { it.value == Day15Object.BOX }.map { it.key.row * 100L + it.key.column }.sum()
}

private fun moveBox(workingGame: MutableMap<Position, Day15Object>,  fromPosition: Position , placeToMove: Position) {

    require(workingGame[placeToMove] == Day15Object.EMPTY)
    workingGame[placeToMove] = workingGame[fromPosition]!!
    workingGame[fromPosition] = Day15Object.EMPTY
}

private fun moveRobot(
    workingGame: MutableMap<Position, Day15Object>,
    fromPosition: Position,
    placeToMove: Position
): Position {
    if(workingGame[placeToMove] != Day15Object.EMPTY)
        throw IllegalArgumentException("Invalid move $fromPosition  $placeToMove ${workingGame[placeToMove]}")
    workingGame[placeToMove] = Day15Object.STARTING
    workingGame[fromPosition] = Day15Object.EMPTY
    return placeToMove
}


fun canMoveBox(
    nextPosition: Position,
    direction: Direction,
    workingGame: MutableMap<Position, Day15Object>
): Pair<Boolean, List<Position>> {
    val boxesToMove = mutableListOf<Position>(nextPosition)
    var currentBox = nextPosition
    while (workingGame[currentBox] == Day15Object.BOX) {
        boxesToMove.add(currentBox)
        currentBox = currentBox.move(direction)
    }
    val canMove = workingGame[currentBox] == Day15Object.EMPTY
    return canMove to boxesToMove
}

fun canMoveBox2(
    nextPosition: Position,
    direction: Direction,
    workingGame: MutableMap<Position, Day15Object>
): Pair<Boolean, List<Day15Object.BIGGER_BOX>> {
    val boxesToMove = mutableListOf<Day15Object.BIGGER_BOX>(workingGame[nextPosition]!! as Day15Object.BIGGER_BOX)
    val boxesToProcess = (workingGame[nextPosition]!! as Day15Object.BIGGER_BOX).allPosition.toMutableList()
    val processed = mutableListOf<Position>()
    while (boxesToProcess.isNotEmpty()) {
        var currentBox = boxesToProcess.removeFirst()

        var nextPositionToProcess = currentBox.move(direction)
//        currentBox.println("Processing $boxesToProcess ${workingGame[nextPositionToProcess]}")
        val item: Day15Object = workingGame[nextPositionToProcess]!!

        when {
            processed.contains(currentBox) -> {
                continue
            }
            item is Day15Object.BIGGER_BOX -> {
                boxesToProcess.addAll(item.allPosition)
                if(!boxesToMove.contains( item ))
                    boxesToMove.add(item)

                processed.add(currentBox)
            }

            item == Day15Object.WALL -> {
                return false to emptyList()
            }

            item == Day15Object.EMPTY -> {}
            else -> throw IllegalArgumentException("Should not be here")
        }
    }

    return true to boxesToMove
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 2028L)
    check(part1(readInputResources(DAY_NAME, "test_2")).println("Part one test result") == 10092L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test_2")).println("Part two test result") == 9021L)
}

private fun part2(input: List<String>): Long {
    val processedInput = input.takeWhile { it.isNotEmpty() }

    val game = processedInput.withIndex().map { (rowIdx, row) ->
        var columnIdx = 0
        row.flatMap { column ->
            when (column) {
                '.' -> listOf(
                    Position(rowIdx, columnIdx++) to Day15Object.EMPTY,
                    Position(rowIdx, columnIdx++) to Day15Object.EMPTY,
                )

                '#' -> listOf(
                    Position(rowIdx, columnIdx++) to Day15Object.WALL,
                    Position(rowIdx, columnIdx++) to Day15Object.WALL,
                )

                '@' -> listOf(
                    Position(rowIdx, columnIdx++) to Day15Object.STARTING,
                    Position(rowIdx, columnIdx++) to Day15Object.EMPTY,
                )

                'O' -> {
                    val firstPosition = Position(rowIdx, columnIdx++)
                    val secondPosition = Position(rowIdx, columnIdx++)
                    val biggerBox = Day15Object.BIGGER_BOX(listOf(firstPosition, secondPosition))
                    listOf(firstPosition to biggerBox, secondPosition to biggerBox)
                }

                else -> throw IllegalArgumentException("Invalid value")
            }
        }
    }.flatten().toMap().println("Game")
    val maxColumn = game.keys.maxOf { it.column } +1
    val maxRow = game.keys.maxOf { it.row }+1

    val movement = input.dropWhile { it.isNotEmpty() }.flatMap {
        it.map {
            when (it) {
                '^' -> Direction.NORTH
                'v' -> Direction.SOUTH
                '<' -> Direction.WEST
                '>' -> Direction.EAST
                else -> throw IllegalArgumentException("Invalid value")
            }
        }
    }.toMutableList().println("Movement")

    val startingPoint = game.filterValues { it == Day15Object.STARTING }.keys.first().println("Starting point")


    val workingGame = game.toMutableMap()
    var currentPosition = startingPoint
    val iteration = 0
    while (movement.isNotEmpty()) {
        printGame(maxRow, maxColumn, workingGame)
        val direction = movement.removeFirst()
        val nextPosition = currentPosition.move(direction)

        when {
            workingGame[nextPosition] == Day15Object.WALL -> {
                "Wall".println()
            }

            workingGame[nextPosition] == Day15Object.EMPTY -> {
                "Empty".println()
                currentPosition = moveRobot(workingGame, currentPosition, nextPosition)
            }

            workingGame[nextPosition] is Day15Object.BIGGER_BOX -> {
                val (canMove, boxesToMove) = canMoveBox2(nextPosition, direction, workingGame)
                boxesToMove.println("canMove: $canMove")
                if (canMove) {

                    boxesToMove.reversed().forEach { firstBox ->
                        moveBiggerBox(firstBox, direction,workingGame)

                        firstBox.println("After move $direction")
                    }
                    currentPosition = moveRobot(workingGame, currentPosition, nextPosition)
                }

            }

            else -> throw IllegalArgumentException("Invalid value ${workingGame[nextPosition]}")

        }
        direction.println("Direction")


    }

    workingGame.println("After game")

    printGame(maxRow, maxColumn, workingGame)
    println("Max row: $maxRow, max column: $maxColumn")
    val bigger = workingGame.values.filterIsInstance(Day15Object.BIGGER_BOX::class.java).toSet()
    bigger.size.println("Bigger boxes")
    return bigger.map {
//        val column = it.allPosition.minOf {
//            it.println("Position column distance")
//
//            min(it.column.println("Column left"), (maxColumn-1 - it.column).println("Column right"))
//        }
//        val row = it.allPosition.minOf {  min(it.row, maxRow-1 - it.row) }
         100L * it.allPosition.minOf { it.row } + it.allPosition.minOf { it.column }
    }.sum()

}

private fun printGame(
    maxRow: Long,
    maxColumn: Long,
    workingGame: MutableMap<Position, Day15Object>
) {
//    (0..<maxRow).map { rowIdx ->
//        (0..<maxColumn).map { columnIdx ->
//            workingGame[Position(column = columnIdx, row = rowIdx)]!!.symbol
//        }.joinToString("")
//    }.joinToString("\n").println()
}

fun moveBiggerBox(biggerBox: Day15Object.BIGGER_BOX, direction : Direction, workingGame: MutableMap<Position, Day15Object>){
    val oldPositions = biggerBox.allPosition

    biggerBox.allPosition = biggerBox.allPosition.map {
        val nextPosition = it.move(direction)
        workingGame[nextPosition] = biggerBox
        nextPosition
    }
    (oldPositions.toSet()-biggerBox.allPosition.toSet()).forEach {
        workingGame[it] = Day15Object.EMPTY
    }

}

