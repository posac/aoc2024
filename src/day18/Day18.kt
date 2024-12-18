package day18

import Direction
import Position
import State
import println
import readInputResources

private const val DAY_NAME = "Day18"

var idxSeq = 0

enum class MazeTile(val symbol: Char) {
    WALL('#'),
    EMPTY('.'),
    START('S'),
    END('E');

    companion object {
        fun fromChar(c: Char) = values().first { it.symbol == c }
    }
}


data class MazePath(
    val pathId: Int,
    val path: MutableList<Position>,
    var currentPosition: Position,
    var currentDirection: Direction,
    var turns: Int = 0,
    var streight: Int = 0
) {

    constructor(mazePath: MazePath) : this(
        idxSeq++,
        path = mazePath.path.toMutableList(),
        currentPosition = mazePath.currentPosition,
        currentDirection = mazePath.currentDirection,
        turns = mazePath.turns,
        streight = mazePath.streight
    )

    fun move(): MazePath {
        currentPosition = currentPosition.move(currentDirection)
        streight++
        return this
    }

    fun rotateLeft(): MazePath {
        return MazePath(this).apply {
            currentDirection = currentDirection.turnLeft()
            turns++
        }
    }

    fun rotateRight(): MazePath {
        return MazePath(this).apply {
            currentDirection = currentDirection.turnLeft().oposit()
            turns++
        }

    }


    fun cost() = turns + streight
}


private fun solveGame(
    input: List<String>,
    rowCount: Int,
    columnCount: Int,
): State<MazePath, MazePath> {
    val game = parseGame(input, rowCount, columnCount)
    val start = game.entries.first { it.value == MazeTile.START }.key.println("Start")
    val startingDirection = Direction.EAST
    val visited = mutableMapOf<Pair<Position, Direction>, MazePath>()


    val startingPath = MazePath(
        idxSeq++,
        mutableListOf(start),
        start,
        startingDirection
    )
    val state = State<MazePath, MazePath>(
        mutableListOf(
            startingPath,
            startingPath.rotateLeft(),
            startingPath.rotateRight()

        )
    )
    while (state.itemsToProcess.isNotEmpty()) {
        val path = state.itemsToProcess.removeFirst()
        val nextPosition = path.currentPosition.move(path.currentDirection)
        if (!visited.containsKey(path.currentPosition to path.currentDirection)) {
            visited[path.currentPosition to path.currentDirection] = path
        } else {
            if (visited[path.currentPosition to path.currentDirection]!!.cost() >= path.cost()) {
                visited[path.currentPosition to path.currentDirection] = path
            } else {
                continue
            }
        }



        when {
            nextPosition in path.path -> {

            }

            game[nextPosition] == MazeTile.WALL -> {
            }

            game[nextPosition] == MazeTile.EMPTY -> {
                path.path.add(nextPosition)
                state.addToProcessing(path.move())
                state.addToProcessing(path.rotateLeft())
                state.addToProcessing(path.rotateRight())
            }

            game[nextPosition] == MazeTile.END -> {
                path.path.add(nextPosition)
                path.currentPosition = nextPosition
                path.streight++
                state.acceptResult(path)
            }

            game[nextPosition] == MazeTile.START -> {

            }

        }

    }
    return state
}

fun parseGame(input: List<String>, rows: Int, column: Int): Map<Position, MazeTile> {
    val game =
        (0..rows + 1).flatMap { rowIdx ->
            (0..column + 1).map { columnIdx ->
                Position(column = columnIdx, row = rowIdx) to when {
                    rowIdx == 0 || rowIdx == rows + 1 || columnIdx == 0 || columnIdx == column + 1 -> MazeTile.WALL
                    else -> MazeTile.EMPTY
                }
            }
        }.toMap().toMutableMap()
    printGame(game)
    input.forEach {
        val (column, row) = it.split(",").map { it.toInt() }
        game[Position(column = column + 1, row = row + 1)] = MazeTile.WALL
    }

    game[Position(column = 1, row = 1)] = MazeTile.START
    game[Position(column = column, row = rows)] = MazeTile.END

    printGame(game)
    return game
}


fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input, 71, 71).println("Part one result:").apply {
        check(this != 140L)
    }
    part2(input, 1024, input.size, 71, 71).println("Part two result:")
}


private fun part1(input: List<String>, rowCount: Int, columnCount: Int): Long {
    val state = solveGame(input.take(1024), rowCount, columnCount)
    return state.result.minOf { it.path.size }.toLong() - 1
}

private fun checkPart1() {

    check(part1(readInputResources(DAY_NAME, "test").take(12), 7, 7).println("Part one test result") == 22L)
}

private fun checkPart2() {
    val test = readInputResources(DAY_NAME, "test")
    check(part2(test, 12, test.size, 7, 7).println("Part two test result") == "6,1")
}

private fun part2(input: List<String>, startFrom: Int, endAt: Int, rowCount: Int, columnCount: Int): String {
    if (startFrom == endAt || startFrom + 1 == endAt) {
        return input[startFrom]
    }

    val simulate = (startFrom + endAt) / 2
    simulate.println("startFrom=$startFrom endAt=$endAt simulate=")

    val state = solveGame(input.take(simulate), rowCount, columnCount)

    if (state.result.isEmpty()) {
        return part2(input, startFrom, simulate, rowCount, columnCount)
    } else {
        return part2(input, simulate, endAt, rowCount, columnCount)
    }

}


private fun printGame(
    workingGame: Map<Position, MazeTile>,
    path: List<Position> = emptyList()
) {
    val maxRow = workingGame.keys.maxOf { it.row }
    val maxColumn = workingGame.keys.maxOf { it.column }
    (0..maxRow).map { rowIdx ->
        (0..maxColumn).map { columnIdx ->

            val position = Position(column = columnIdx, row = rowIdx)
            if (position in path) "O"
            else
                workingGame[position]!!.symbol
        }.joinToString("")
    }.joinToString("\n").println()
}