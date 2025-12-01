package day20

import Direction
import Position
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import parsePositionGame
import println
import readInputResources
import kotlin.math.abs

private const val DAY_NAME = "2024/Day20"


fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:").apply {
        check(this > 1350L)
    }
    part2(input).println("Part two result:").apply {
        check(this < 2089215L)
        check(this > 997001L)
    }
}


private fun part1(input: List<String>): Long {

    return getAllCheats(input).filter { it.saving >= 100 }.count().toLong()
}

data class Bridge(val from: Position, val to: Position, val saving: Int)

private fun getAllCheats(input: List<String>): List<Bridge> {
    val (game, path) = parseGame(input)

    return path.withIndex().flatMap { (index, position) ->
        listOf(
            Direction.NORTH,
            Direction.SOUTH,
            Direction.EAST,
            Direction.WEST,
        )
            .filter { game[position.move(it)] == '#' }
            .map { position.move(it, 2) }
            .map {
                path.indexOf(it) to it
            }
            .filter { it.first > index }
            .map {
                Bridge(from = position, to = it.second, saving = it.first - index - 2)
            }


    }

}

private fun getAllCheatsPart2(input: List<String>, cheatingThreshold: Int): List<Bridge> {
    val (game, path) = parseGame(input)

    return runBlocking(Dispatchers.Default) {
        path.withIndex().map { (index, position) ->
            async {
                sequence<Pair<Position, Int>> {
                    (-cheatingThreshold..cheatingThreshold).forEach { row ->
                        (-cheatingThreshold +abs(row)..cheatingThreshold-abs(row)).forEach { column ->
//                            println("$row $column ${abs(column) + abs(row)}" )
                            yield(position.move(Direction.NORTH, row.toLong())
                                .move(Direction.EAST, column.toLong()) to abs(column) + abs(row))
                        }
                    }

                }.map {
                        path.indexOf(it.first) to it
                    }
                    .filter { it.first > index }
                    .map {
                        Bridge(from = position, to = it.second.first, saving = it.first - index - it.second.second)
                    }.toList()


            }
        }
            .awaitAll().flatten()
    }

}

private fun parseGame(input: List<String>): Pair<Map<Position, Char>, List<Position>> {
    val game = parsePositionGame(input) { it }

    val startingPoint = game.keys.first { game[it] == 'S' }

    val path = buildList {
        var currentPoint = startingPoint
        while (game[currentPoint] != 'E') {
            add(currentPoint)

            currentPoint =
                currentPoint.allAround().entries.first { game[it.value] != '#' && !this.contains(it.value) }.value

        }
        add(currentPoint)
    }
    return Pair(game, path)
}

private fun checkPart1() {
    getAllCheats(readInputResources(DAY_NAME, "test")).let { cheats ->
        cheats.forEach {
            println(it)
        }
        val cheatGroups = cheats.groupBy { it.saving }

        cheatGroups.forEach { key, value ->
            key.println("Saved $key ${value.size}")

        }
        check(cheatGroups.size == 11)
        check(cheatGroups[2]!!.size == 14)
        check(cheatGroups[4]!!.size == 14)
        check(cheatGroups[6]!!.size == 2)
        check(cheatGroups[8]!!.size == 4)
        check(cheatGroups[10]!!.size == 2)
        check(cheatGroups[12]!!.size == 3)
        check(cheatGroups[20]!!.size == 1)
        check(cheatGroups[36]!!.size == 1)
        check(cheatGroups[38]!!.size == 1)
        check(cheatGroups[40]!!.size == 1)
        check(cheatGroups[64]!!.size == 1)

    }


}


private fun checkPart2() {
    getAllCheatsPart2(readInputResources(DAY_NAME, "test"), 20).let { cheats ->
        cheats.forEach {
            println(it)
        }
        val cheatGroups = cheats.filter { it.saving >= 50 }.groupBy { it.saving }

        cheatGroups.forEach { key, value ->
            key.println("Saved $key ${value.size}")

        }
        check(cheatGroups.size == 14)
        check(cheatGroups[50]!!.size == 32)
        check(cheatGroups[52]!!.size == 31)
        check(cheatGroups[54]!!.size == 29)
        check(cheatGroups[56]!!.size == 39)
        check(cheatGroups[58]!!.size == 25)
        check(cheatGroups[60]!!.size == 23)
        check(cheatGroups[62]!!.size == 20)
        check(cheatGroups[64]!!.size == 19)
        check(cheatGroups[66]!!.size == 12)
        check(cheatGroups[68]!!.size == 14)
        check(cheatGroups[70]!!.size == 12)
        check(cheatGroups[72]!!.size == 22)
        check(cheatGroups[74]!!.size == 4)
        check(cheatGroups[76]!!.size == 3)

    }

}

private fun part2(input: List<String>): Long {
    return getAllCheatsPart2(input, cheatingThreshold = 20).filter { it.saving >= 100 }.count().toLong()

}



private fun printGame(
    workingGame: Map<Position, Char>,
    path: List<Position> = emptyList()
) {
    val maxRow = workingGame.keys.maxOf { it.row }
    val maxColumn = workingGame.keys.maxOf { it.column }
    (0..maxRow).map { rowIdx ->
        (0..maxColumn).map { columnIdx ->

            val position = Position(column = columnIdx, row = rowIdx)
            if (position in path) "O"
            else
                workingGame[position]!!
        }.joinToString("")
    }.joinToString("\n").println()
}
