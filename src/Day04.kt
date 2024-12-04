private const val DAY_NAME = "Day04"
fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}

val modifiers = listOf(
    (-1 to 0),
    (1 to 0),
    (0 to -1),
    (0 to 1),
    (-1 to -1),
    (-1 to 1),
    (1 to 1),
    (1 to -1),

    )

private fun part1(input: List<String>): Long {
    var chars = input.map { it.toCharArray().map { it }.toMutableList() }.toMutableList()
    val searchWord = "XMAS"
    return chars.mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, char ->
            checkWord(
                chars, searchWord, listOf(rowIndex to columnIndex)
            ) +
                    checkWord(
                        chars,
                        searchWord.reversed(),

                        listOf(rowIndex to columnIndex)
                    )
        }
    }.flatten().flatten().filter { it.isNotEmpty() }.toSet().also {
//        it.forEach(::println)
    }.size.toLong() / 2


}

private fun checkWord(
    chars: List<List<Char>>,
    searchWord: String,
    possitionLog: List<Pair<Int, Int>>,
    direction: Pair<Int, Int>? = null,

    ): List<List<Pair<Int, Int>>> {
    val char = chars.get(possitionLog.last().first, possitionLog.last().second)
    return when {
        searchWord.isEmpty() -> listOf(possitionLog)
        char == searchWord[0] && direction == null -> {
            modifiers.flatMap {
                val nextPosition = possitionLog.last().first + it.first to possitionLog.last().second + it.second
                checkWord(
                    chars,
                    searchWord.drop(1),
                    possitionLog + nextPosition,
                    it,
                )
            }
        }

        char == searchWord[0] && direction != null -> {
            val nextPosition =
                possitionLog.last().first + direction.first to possitionLog.last().second + direction.second
            checkWord(
                chars,
                searchWord.drop(1),
                possitionLog + nextPosition,
                direction,
            )
        }

        else -> emptyList()
    }
}


private fun List<List<Char>>.get(row: Int, column: Int): Char? {
    return if (row >= 0 && row < size && (column >= 0 && column < get(row).size))
        get(row).get(column)
    else null

}


private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 18L)
    check(part1(readInputResources(DAY_NAME, "input")).println("Part one test result") == 2633L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 9L)
}

private fun part2(input: List<String>): Long {
    var chars = input.map { it.toCharArray().map { it }.toMutableList() }.toMutableList()
    val searchWord = "XMAS"
    return chars.mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, char ->
            if (char == 'A') {git commit --author="Posac" -m "Your commit message"
                val oneDiag =
                    checkWord(chars, "MAS", listOf(rowIndex - 1 to columnIndex - 1), direction = (1 to 1)) + checkWord(
                        chars,
                        "SAM",
                        listOf(rowIndex - 1 to columnIndex - 1),
                        direction = (1 to 1)
                    ).println()

                val anotherDiag = checkWord(
                    chars,
                    "MAS",
                    listOf(rowIndex - 1 to columnIndex + 1),
                    direction = (1 to -1)
                ) + checkWord(chars, "SAM", listOf(rowIndex - 1 to columnIndex + 1), direction = (1 to -1)).println()


                if (oneDiag.isNotEmpty() && anotherDiag.isNotEmpty()) {
                    listOf(rowIndex to columnIndex)
                } else emptyList()

            } else emptyList()

        }
    }.flatten().filter { it.isNotEmpty() }.toSet().also {
        it.forEach(::println)
    }.size.toLong()


}

