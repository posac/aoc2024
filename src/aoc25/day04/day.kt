package aoc25.day04

import println
import readInputResources

private const val DAY_NAME = "2025/Day04"



fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    val rowsNumber = input.size
    val colsNumber = input.first().length

    val result = IntRange(0, rowsNumber-1).flatMap { rowIdx ->
        IntRange(0, colsNumber-1).map { colIdx ->
            if(input[rowIdx][colIdx] != '@') return@map 0
            val isFine = input.getAdjactentCells(rowIdx, colIdx).filter { it == '@' }.size < 4
            isFine.println("($rowIdx, $colIdx) is fine:)")
            if(isFine)
                1
            else 0
        }


    }


    return result.sum().toLong()
}

private fun List<String>.getAdjactentCells(row: Int, col: Int): List<Char> {
    val directions = listOf(
        -1 to -1, -1 to 0, -1 to 1,
         0 to -1,          0 to 1,
         1 to -1,  1 to 0, 1 to 1
    )
    val adjacentCells = mutableListOf<Char>()
    for ((dRow, dCol) in directions) {
        val newRow = row + dRow
        val newCol = col + dCol
        if (newRow in indices && newCol in this[newRow].indices) {
            adjacentCells.add(this[newRow][newCol])
        }
    }
    return adjacentCells
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 13L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 43L)
}

private fun part2(input: List<String>): Long {
    val rowsNumber = input.size
    val colsNumber = input.first().length

    val result = processGame(rowsNumber, colsNumber, input)

    return result
}

private fun processGame(
    rowsNumber: Int,
    colsNumber: Int,
    input: List<String>
): Long {
    val result = IntRange(0, rowsNumber - 1).flatMap { rowIdx ->
        IntRange(0, colsNumber - 1).mapNotNull { colIdx ->
            if (input[rowIdx][colIdx] != '@') return@mapNotNull null
            val isFine = input.getAdjactentCells(rowIdx, colIdx).filter { it == '@' }.size < 4
            if (isFine)
                rowIdx to colIdx
            else null
        }
    }

    val positionsToChange = result.groupBy { it.first }.mapValues { it.value.map { it.second }.toSet() }

    val newInput = input.mapIndexed { rowIdx, s ->
        val positionToAdj = positionsToChange[rowIdx] ?: emptySet()
        s.mapIndexed { colIdx, c -> if (colIdx in positionToAdj) '.' else c }.joinToString("")
    }
    return result.size.toLong() + if(result.isNotEmpty()) processGame(rowsNumber, colsNumber, newInput) else 0
}


