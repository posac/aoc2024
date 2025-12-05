package aoc25.day05

import println
import readInputResources
import java.io.File
import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds

private const val DAY_NAME = "2025/Day05"


fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    val game = input.takeWhile { it.isNotBlank() }.map {
        val res = it.split("-").map { it.toLong() }
        LongRange(res[0], res[1])
    }.sortedBy { it.first }

    val numbersToCheck = input.dropWhile { it.isBlank() or it.contains("-") }.map { it.toLong() }.sorted()

    val newGame = joinOverlap(game)
    return numbersToCheck.mapNotNull { number ->
        newGame
            .first
            .filter { it.first <= number }
            .firstOrNull { number in it }
    }.size.toLong()
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 3L)
    check(part1(readInputResources(DAY_NAME, "input")).println("Part one test result") == 885L)
}

private fun checkPart2() {
//    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 14)
//    check(part2(readInputResources(DAY_NAME, "test2")).println("Part two test result") ==  14L)
//    check(part2(readInputResources(DAY_NAME, "input")).println("Part two test result") !=  525458782220470L)
    check(part2(readInputResources(DAY_NAME, "input")).println("Part two test result") >   330684820916250L)
    check(part2(readInputResources(DAY_NAME, "input")).println("Part two test result") >   341401148500275L)
}

private fun part2(input: List<String>): Long {
    val game = input.takeWhile { it.isNotBlank() }.map {
        val res = it.split("-").map { it.toLong() }
        LongRange(res[0], res[1])
    }.sortedWith(compareBy({ it.first }, { it.last }))

    print(game.size)
    val newGame = joinOverlap(game)


    File("debug_init.txt").writeText(game.joinToString("\n") { "${it.first}-${it.last}" })
    File("debug_merged.txt").writeText(newGame.first.joinToString("\n") { "${it.first}-${it.last}" })


    return newGame.first.map { it.last - it.first + 1 }
        .reduce { acc, n ->
            val res = acc + n
            require(res >= 0)
            require(res > acc)
            require(res > n)
            res
        }
}


private fun joinOverlap(initialGame: List<LongRange>, ref :String ="root"):  Pair<List<LongRange>, Int> {
    val game = initialGame.sortedWith(compareBy({ it.first }, { it.last })).println("[$ref]Game")
    if (game.size == 1) return game to 0
    val needsJoining = needsJoining(game)

    if (!needsJoining) return game.println("[$ref]No overlap") to 0
    if(game.size == 2) {
        if(game[0].last >= game[1].first)
            return listOf(LongRange(min(game[0].first, game[1].first), max(game[0].last, game[1].last))).println("[$ref]Merging") to 1
        else
            return game to 0
    }
    val mid = game.size / 2
    val left = joinOverlap(game.subList(0, mid), ref+"/L")
    val right = joinOverlap(game.subList(mid, game.size), ref+"/R")


    val midJoined = joinOverlap(
        left.first.takeLast(1) + right.first.take(1),
        ref + "/M"
    )
    val result = ((left.first.dropLast(1) + midJoined.first + right.first.drop(1)))


    val resultList=  result
        .sortedWith(compareBy({ it.first }, { it.last }))
        .println("[$ref]Merged")
//493548713557
//14138223877747

    require(needsJoining(resultList) ==false)
    val mergeCounts = left.second + right.second + midJoined.second
    println("[$ref] initialSize=${initialGame.size} resultSize=${resultList.size} mergeCounts=$mergeCounts")
    require(initialGame.size == resultList.size + mergeCounts)
    return resultList to mergeCounts


}

private fun needsJoining(game: List<LongRange>): Boolean {
    val needsJoining = game
        .withIndex().zipWithNext().any { range ->
            range.first.value.last >= range.second.value.first
        }
    return needsJoining
}
