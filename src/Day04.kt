private const val DAY_NAME = "Day04"
fun main() {
    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


val neededLetters = mapOf(
    "X" to listOf("M"),
    "M" to listOf("X", "A"),
    "A" to listOf("M", "S"),
    "S" to listOf("A")

)

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
    var chars = input.map { it.toCharArray().map { it.toString() as String? }.toMutableList() }.toMutableList()
    var modified = true


    while (modified) {
        modified = false

        for( row in 0..<chars.size){
            for(column in 0..<chars[row].size) {
                val currentLetter = chars.get(row,column)
                if(currentLetter==null)
                    continue

                val neededArround = neededLetters[currentLetter]!!

                var anyMatching = false
                for (mod in modifiers) {
                    val charArround = chars.get(row + mod.first, column + mod.second)
                    if (charArround != null && neededArround.contains(charArround)) {
                        anyMatching = true
                    }
                }

                if (anyMatching) {
                    currentLetter
                } else {
                    chars[row][column]=null
                    "Dropping letter $currentLetter ($row, $column)".println()
                    null
                }
            }
        }


        ("\n"+chars.map { it.map { it?:'.' }.joinToString("")}.joinToString("\n")).println("\n")
    }

    return 0
}

private fun MutableList<MutableList<String?>>.get(row: Int, column: Int): String? {
    return if (row >= 0 && row < size && (column >= 0 && column < get(row).size))
        get(row).get(column)
    else null

}




private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 18L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 281L)
}

private fun part2(input: List<String>): Long = input.size.toLong()


