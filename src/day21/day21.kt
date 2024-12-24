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
        val numeric = movementsOnKeyboard(it, numbericKeyboad)
        val secondMinimal = robots(numeric, 4)

//        val secondMinimal = robots.minOf { it.length }
        val number = it.filter { it.isDigit() }.toInt()
        number.println("Number")

        number * secondMinimal.toLong()

    }


}


private fun robots(numeric: List<String>, robotsCount: Int): Int {


    val score = numeric.minOf {
        println("solution: $it")
        it.split("A").sumOf {
            if (it == "")
                0
            else {
                println("Split: $it")
                movementsOnKeyboard(it + "A", directionKeyboard).minOf {
                    if (it == "")
                        0
                    else
                        it.split("A")
                            .sumOf {
                                movementsOnKeyboard(it + "A", directionKeyboard)
                                    .minOf { it.length }
                            }
                }
            }
        }
    }
//    var robots = listOf( numeric.first())
//    robots.println("Initial:")
//    repeat(2) {
//        robots = robots.flatMap { movementsOnKeyboard(it, directionKeyboard) }
//        robots.take(5).println("Iteration:")
//
//
//
//    }

//        .map { it.map { score[it]!! }.joinToString("") }
//
//    val robots = numeric.map { it.map { score[it] }.joinToString("") }
    return score
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

val cache: MutableMap<CacheKeyMoves, Set<List<Direction>>> = mutableMapOf()

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
    return cache.getOrPut(
        CacheKeyMoves(
            game,
            columnCount,
            rowCount,
            columnDirection,
            rowDirection,
            currentPosition,
            destination,
            path
        )
    ) {
        when {
            game[currentPosition]!! == '_' -> emptySet()

            currentPosition == destination -> setOf(path)
            else -> {
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
// 1409445
// 126384
// 454024
// 307397
// 579506
// 7324992
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 281L)
}

private fun part2(input: List<String>): Long {
    return 0L
}

val directionCaches = mutableMapOf<Pair<String, Int>, Pair<String, List<String>>>()
fun evaluateDirections(direction: String, time: Int): Pair<String, List<String>> {
    return directionCaches.getOrPut(direction to time) {
        when (time) {
            1 -> {
                val items = movementsOnKeyboard(direction, directionKeyboard)
                items.minBy { it.length } to items
            }

            else -> {
                val prevItems = evaluateDirections(direction, time - 1)
                val items = prevItems.second.flatMap { movementsOnKeyboard(it, directionKeyboard) }
                items.minBy { it.length }!! to items
            }
        }
    }


}


//980A ^^^A
//< = <v<A>>^A
//AAvA^A<vA<AA>>^AvAA<^A>A<v<A>A>^AAAvA<^A>A<vA>^A<A>A


//029A:

//    v<A<A>^>A<Av>A^A
//R1: <vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A
//R2:   v <<   A >>  ^ A   <   A > A  v  A   <  ^ AA > A   < v  AAA >  ^ A
//N :          <       A       ^   A     >        ^^   A        vvv      A
//N:                   0           2                   9                 A


/*


v<A<AA>^>AvA^<Av>A^Av<<A>^>AvA^Av<<A>^>AAv<A>A^A<A>Av<A<A>^>AAA<Av>A^A
  v <<   A >  ^  > A<A>A<AAv>A^Av<AAA^>A
         <         A^A^^>AvvvA

Initial: [<A^A^^>AvvvA]
Iteration: [v<<A>^>A<A>A<AAv>A^Av<AAA^>A, v<<A>^>A<A>A<AAv>A^Av<AAA>^A, v<<A>^>A<A>A<AAv>A^A<vAAA^>A, v<<A>^>A<A>A<AAv>A^A<vAAA>^A, v<<A>^>A<A>A<AA>vA^Av<AAA^>A]
Iteration: [v<A<AA>^>AvA^<Av>A^Av<<A>^>AvA^Av<<A>^>AAv<A>A^A<A>Av<A<A>^>AAA<Av>A^A, v<A<AA>^>AvA^<Av>A^Av<<A>^>AvA^Av<<A>^>AAv<A>A^A<A>Av<A<A>^>AAA<A>vA^A, v<A<AA>^>AvA^<Av>A^Av<<A>^>AvA^Av<<A>^>AAv<A>A^A<A>Av<A<A>>^AAA<Av>A^A, v<A<AA>^>AvA^<Av>A^Av<<A>^>AvA^Av<<A>^>AAv<A>A^A<A>Av<A<A>>^AAA<A>vA^A, v<A<AA>^>AvA^<Av>A^Av<<A>^>AvA^Av<<A>^>AAv<A>A^A<A>A<vA<A>^>AAA<Av>A^A]

 */