package day21

import Direction
import Position
import parsePositionGame
import println
import readInputResources
import kotlin.math.abs

private const val DAY_NAME = "Day21"


fun main() {
    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:").apply {
        check(this == 248684L)
    }
//    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    return input.sumOf {
        val numeric =  movementsOnKeyboard(it, numbericKeyboad)
        val firstDirection = numeric.flatMap {  movementsOnKeyboard(it, directionKeyboard)}
        val minimal = firstDirection.minOf { it.length }
        val secondDirection = firstDirection.filter { it.length == minimal }.flatMap {
            movementsOnKeyboard(it, directionKeyboard)
        }
        val secondMinimal = secondDirection.minOf { it.length }
        val number = it.filter { it.isDigit() }.toInt()
        number.println("Number")

        number*secondMinimal.toLong()

    }


}

val directionKeyboard = parsePositionGame(
    listOf(
        "_^A",
        "<v>",
    )
) { it }

val numbericKeyboad = parsePositionGame(
    listOf(
        "789",
        "456",
        "123",
        "_0A",
    )
) { it }


val numericKeyboardCharToPosition = numbericKeyboad.entries.associate { it.value to it.key }

fun movementsOnKeyboard(code: String, keyboard: Map<Position, Char>): List<String> {
    val paths: MutableList<Set<List<Direction>>> = mutableListOf()
    val reversedKeyboard = keyboard.entries.associate { it.value to it.key }
    val initialPosition = reversedKeyboard['A']

    var currentPosition = initialPosition
    val positionsToReach = code.map { reversedKeyboard[it] }.toMutableList()

    while (positionsToReach.isNotEmpty()) {
        val destination = positionsToReach.removeFirst()!!

        val columnCount = currentPosition!!.column - destination!!.column
        val rowCount = currentPosition!!.row - destination.row


        val columnDirection = if (columnCount > 0) Direction.WEST else Direction.EAST
        val rowDirection = if (rowCount > 0) Direction.NORTH else Direction.SOUTH

        paths.add(
            generateAllMoves(
                game = keyboard,
                columnCount = abs(columnCount),
                rowCount = abs(rowCount),
                columnDirection = columnDirection,
                rowDirection = rowDirection,
                currentPosition = currentPosition,
                destination = destination
            )
        )
        currentPosition = destination
    }

    paths.map {
        it.first().map { it.display() } + "A"
    }.joinToString("")


    return run {
        var allStrings = mutableListOf<String>()
        while (paths.isNotEmpty()) {
            val path = paths.removeFirst()
            val allPaths = path.map { movement ->
                movement.map { it.display() }.joinToString("") + "A"
            }
            if (allStrings.isEmpty()) {
                allStrings.addAll(allPaths)
            } else {
                allStrings = allStrings.flatMap { string ->
                    allPaths.map { string + it }
                }.toMutableList()
            }
        }
        allStrings

    }


}

data class CacheKeyMoves(
    val game: Map<Position, Char>,
    val columnCount: Long,
    val rowCount: Long,
    val columnDirection: Direction,
    val rowDirection: Direction,
    val currentPosition: Position,
    val destination: Position,
    val path: List<Direction> = mutableListOf()
)

val cache : MutableMap<CacheKeyMoves, Set<List<Direction>>>  = mutableMapOf()

fun generateAllMoves(
    game: Map<Position, Char>,
    columnCount: Long,
    rowCount: Long,
    columnDirection: Direction,
    rowDirection: Direction,
    currentPosition: Position,
    destination: Position,
    path: List<Direction> = mutableListOf()
): Set<List<Direction>> {
    return cache.getOrPut(CacheKeyMoves(game, columnCount, rowCount, columnDirection, rowDirection, currentPosition, destination, path)) {
        when {
            game[currentPosition]!! == '_' ->  emptySet()

            currentPosition == destination -> setOf( path)
            else ->  {
                val result = mutableSetOf<List<Direction>>()

                if (rowCount != 0L)
                    result.addAll(
                        generateAllMoves(
                            game = game,
                            columnCount = columnCount,
                            rowCount = rowCount - 1,
                            columnDirection = columnDirection,
                            rowDirection = rowDirection,
                            currentPosition = currentPosition.move(rowDirection),
                            destination = destination,
                            path = path + rowDirection
                        )
                    )

                if (columnCount != 0L)
                    result.addAll(
                        generateAllMoves(
                            game = game,
                            columnCount = columnCount - 1,
                            rowCount = rowCount,
                            columnDirection = columnDirection,
                            rowDirection = rowDirection,
                            currentPosition = currentPosition.move(columnDirection),
                            destination = destination,
                            path = path + columnDirection
                        )
                    )

                result
            }
        }
    }

}


private fun checkPart1() {

    movementsOnKeyboard("029A", numbericKeyboad).let {
        it.forEach(::println)
        check(it.contains("<A^A>^^AvvvA"))
        check(it.contains("<A^A^>^AvvvA"))
        check(it.contains("<A^A^^>AvvvA"))
    }

    movementsOnKeyboard("13", numbericKeyboad).let {
        it.forEach(::println)
    }

    movementsOnKeyboard("7", numbericKeyboad).let {
        check(!it.contains("<<^^^A"))
    }
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 126384L)

}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 281L)
}

private fun part2(input: List<String>): Long {
    return 0L
}

val directionCaches = mutableMapOf<Pair<Char, Int>, String>()
fun evaluateDirections(direction: Char, time: Int): String {
    return directionCaches.getOrPut(direction to time) {
        val posibilities = when (direction) {
            '^' -> movementsOnKeyboard("^A", directionKeyboard)
            'v' -> movementsOnKeyboard("vA", directionKeyboard)
            '>' -> movementsOnKeyboard(">A", directionKeyboard)
            '<' -> movementsOnKeyboard("<A", directionKeyboard)
            'A' -> movementsOnKeyboard("A", directionKeyboard)
            else -> throw IllegalArgumentException("Invalid direction $direction")
        }

        if (time == 1)
            posibilities.minBy { it.length }.println("[$time]Min for direction $direction")
        else
            posibilities.map { it.map { evaluateDirections(it, time - 1) }.joinToString("") }
                .minBy { it.length }
                .println("[$time]Min for direction $direction")
    }


}


//980A ^^^A
//< = <v<A>>^A
//AAvA^A<vA<AA>>^AvAA<^A>A<v<A>A>^AAAvA<^A>A<vA>^A<A>A


//029A:
//<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A
//  v <<   A >>
