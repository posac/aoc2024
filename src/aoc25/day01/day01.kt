package aoc25.day01

import println
import readInputResources

private const val DAY_NAME = "2025/Day01"

enum class Direction {
    LEFT, // down
    RIGHT //up
}

data class Rotation(
    val direction: Direction,
    val distance: Int
)

data class State(
    val position : Int,
    val zeroAccum : Int = 0
)

fun main() {

    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>, startingPoint : Int = 50, size : Int = 100): Long {
    val game = parseGame(input)


    return game.fold(State(startingPoint)) { state, rotation ->
        val newPosition = (state.position + (if(rotation.direction == Direction.LEFT) -1 else 1) * rotation.distance) % size
        state.copy(
            position = newPosition,
            zeroAccum = state.zeroAccum + if(newPosition == 0) 1 else 0
        )
    }.zeroAccum.toLong()

}

private fun parseGame(input: List<String>): List<Rotation> = input.map {
    val direction = if (it[0] == 'L') Direction.LEFT else Direction.RIGHT
    Rotation(direction, it.substring(1).toInt())
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 3L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 6L)
    check(part2(readInputResources(DAY_NAME, "test2")).println("Part two test result") == 3L)
    check(part2(readInputResources(DAY_NAME, "test3")).println("Part two test result") == 12L)
    check(part2(readInputResources(DAY_NAME, "input")).println("Part two test result") != 2454L)
    check(part2(readInputResources(DAY_NAME, "input")).println("Part two test result") != 5475L)
}

private fun part2(input: List<String>, startingPoint : Int = 50, size : Int = 100): Long {
    val game = parseGame(input)


    return game.fold(State(startingPoint)) { state, rotation ->
        val distance = rotation.distance
        val turns = rotation.distance / size
        val refinedDistance = distance - turns * size

        val newPosition = (state.position + (if(rotation.direction == Direction.LEFT) -1 else 1) * refinedDistance)
        val zeroAccumAddOn = turns + when{
            state.position == 0 -> 0
            newPosition >= size -> newPosition / size
            newPosition <= 0 -> (newPosition*-1) / size + 1
            else -> 0
        }
        val refined =  if(newPosition % size >= 0 ) newPosition % size else size + newPosition % size
        rotation.println("zeroAccumAddOn=$zeroAccumAddOn newPosition=$newPosition refined=$refined currentPosition=${state.position}")
        state.copy(
            position = refined,
            zeroAccum = state.zeroAccum + zeroAccumAddOn
        )
    }.zeroAccum.toLong()

}

