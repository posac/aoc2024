package day16

import Direction
import Position
import State
import parsePositionGame
import println
import readInputResources

private const val DAY_NAME = "2024/Day16"

enum class MazeTile(val symbol: Char) {
    WALL('#'),
    EMPTY('.'),
    START('S'),
    END('E');

    companion object {
        fun fromChar(c: Char) = values().first { it.symbol == c }
    }
}

fun main() {
//    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
//    part1(input).println("Part one result:")
    part2(input).println("Part two esult:")
}

var idxSeq = 0

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

    fun cost()  = 1000L*turns+streight
}


private fun part1(input: List<String>): Long {
    val state = solveGame(input)

    return state.result.map { it.cost() }.minOrNull()!!.toLong()
}

private fun solveGame(input: List<String>): State<MazePath, MazePath> {
    val game = parsePositionGame(input) { MazeTile.fromChar(it) }
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
//            .println("Processing:")
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
//                state.addToProcessing(path.rotateLeft())
//                state.addToProcessing(path.rotateRight())
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

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 7036L)
    check(part1(readInputResources(DAY_NAME, "test2")).println("Part one test result") == 11048L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 45L)
    check(part2(readInputResources(DAY_NAME, "test2")).println("Part two test result") == 64L)
}


private fun part2(input: List<String>): Long {
  val state = solveGame(input)
  val bestPathScore = state.result.map { it.cost() }.minOrNull()!!.toLong()

    val bestPaths = state.result.filter { it.cost() == bestPathScore }

    val positions = bestPaths.flatMap { it.path }.toSet()
    input.withIndex().forEach { (rowIdx, row) ->
        row.withIndex().forEach { (columnIdx, column) ->
            if (Position(rowIdx, columnIdx) in positions) {
                print("O")
            } else {
                print(column)
            }
        }
        println()

    }

    return positions.size.toLong()
}


