import kotlin.math.min

private const val DAY_NAME = "Day09"
fun main() {
//    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
//    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    val memory = readMemory(input)

    var indexOfFirstNull: Int
    var lastIndexOfNotNull: Int
    do {
        indexOfFirstNull = memory.indexOfFirst { it == null }
        lastIndexOfNotNull = memory.indexOfLast { it != null }
        if (indexOfFirstNull < lastIndexOfNotNull) {
            memory[indexOfFirstNull] = memory[lastIndexOfNotNull]
            memory[lastIndexOfNotNull] = null
        }
    } while (indexOfFirstNull < lastIndexOfNotNull)
    memory.println()

    return memory.filterNotNull().mapIndexed { index, i -> index.toLong() * i!! }.sum()
}

private fun readMemory(input: List<String>): MutableList<Int?> {
    var free = false
    var id = 0
    val memory = input.first().flatMap {
        val sizeOfBlock = it.digitToInt()
        if (free) {
            free = false
            (1..sizeOfBlock).map { null }
        } else {
            free = true

            val result = (1..sizeOfBlock).map { id }
            id++
            result
        }
    }.toMutableList().println()
    return memory
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 1928L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 2858L)
}

private fun part2(input: List<String>): Long {
    val memory = readMemory(input)
    var indexOfFirstNull: Int
    var lastIndexOfNotNull: Int
    var skipIndex = memory.size
    var itemsToProcess = memory.filterNotNull().toMutableSet()
//    var workingMemeory=memory.toMutableList()
    do {
        memory


        lastIndexOfNotNull = memory.filterIndexed { index, i -> index < skipIndex }.indexOfLast { it != null }
        memory[lastIndexOfNotNull]
        val blockSize = memory.filterIndexed { index, i -> index <= min(lastIndexOfNotNull, skipIndex) }
            .takeLastWhile { it == memory[lastIndexOfNotNull] }.count()

        indexOfFirstNull =
            memory.mapIndexed { index, i -> index to ((memory.size > index + blockSize) && (0..<blockSize).all { memory[index + it] == null }) }
                .filter { it.second }.minOfOrNull { it.first } ?: -1

        indexOfFirstNull

        if (indexOfFirstNull != -1 && indexOfFirstNull < lastIndexOfNotNull) {
            val nullBlockSize =
                memory.filterIndexed { index, i -> index >= indexOfFirstNull }.takeWhile { it == null }.count()


            if (nullBlockSize >= blockSize) {
                (0..<blockSize).forEach {
                    memory[indexOfFirstNull + it] = memory[lastIndexOfNotNull - it]
                    memory[lastIndexOfNotNull - it] = null
                }


            }


        } else {
            skipIndex = lastIndexOfNotNull - blockSize + 1
            skipIndex
        }

        itemsToProcess.remove(memory[lastIndexOfNotNull])
        itemsToProcess
    } while (itemsToProcess.isNotEmpty())


    return memory.mapIndexedNotNull { index, i -> if (i == null) null else index.toLong() * i!! }.sum()
}


