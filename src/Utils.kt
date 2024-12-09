import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines


fun readInputResources(day: String, name: String) = Path("resources/${day}/$name.txt").readLines()


/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun <T> T.println(prefixMessage: String = ""): T = apply {
    kotlin.io.println("${prefixMessage} ${this}")
}

data class Position(val row: Long, val column: Long) {
    constructor(row: Int, column: Int) : this(row.toLong(), column.toLong())

    fun south(times: Long = 1) = Direction.SOUTH to copy(row = row + times)
    fun north(times: Long = 1) = Direction.NORTH to copy(row = row - times)
    fun east(times: Long = 1) = Direction.EAST to copy(column = column + times)
    fun west(times: Long = 1) = Direction.WEST to copy(column = column - times)

    fun allAround() = mapOf(
        west(),
        east(),
        north(),
        south()
    )

    fun transpose() = Position(row = column, column = row)

    fun move(direction: Direction, times: Long = 1) = when (direction) {
        Direction.NORTH -> north(times)
        Direction.EAST -> east(times)
        Direction.SOUTH -> south(times)
        Direction.WEST -> west(times)
    }.second
}

enum class Direction() {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    fun oposit() = when (this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
    }

    fun turnLeft() = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }
}


fun <T> List<T>.combination(): List<Pair<T, T>> = dropLast(1).mapIndexed { index, first ->
    this.drop(index + 1).map { second ->
        first to second
    }
}.flatten()

fun <T> Map<Int, List<T>>.iterateOverCombination(): Sequence<List<T>> {
    val map = this
    return sequence {
        val indexes = keys.associateWith { 0 }.toMutableMap()
        var shouldContinue = true
        val sortedKeys = keys.sorted()
        while (shouldContinue) {
            yield(sortedKeys.map { map[it]!![indexes[it]!!]!! })
            shouldContinue = (sortedKeys.fold(1) { acc, item ->
                if (acc == 0)
                    0
                else
                    if (indexes[item]!! < map[item]!!.size - 1) {
                        indexes[item] = indexes[item]!! + acc
                        0
                    } else {
                        indexes[item] = 0
                        1
                    }
            } == 0)
        }

    }
}


fun List<String>.splitByEmptyLine(): List<List<String>> {
    var batch = mutableListOf<String>()
    var inputData = this
    return sequence<List<String>> {
        var batch = mutableListOf<String>()
        inputData.forEach {
            if (it.isEmpty()) {
                yield(batch)
                batch = mutableListOf()
            } else {
                batch.add(it)
            }
        }
        yield(batch)
    }.toList()
}


fun <T> parsePositionGame(input: List<String>, valueParser: (Char) -> T): Map<Position, T> {
    return input.mapIndexed { rowIdx, row ->
        row.mapIndexed { columnIdx, column -> Position(rowIdx, columnIdx) to valueParser(column) }
    }.flatten().toMap().println("Game")
}


data class State<T, S>(
    val itemsToProcess: MutableList<T>,
    val result: MutableList<S>
) {
    fun addToProcessing(next: T) {
        itemsToProcess.add(next)
    }

    fun acceptResult(current: S) {
        result.add(current)
    }

    constructor(startingItem: List<T>) : this(startingItem.toMutableList(), mutableListOf())
}

inline fun <T, S> processItems(state: State<T, S>, crossinline process: (T, State<T, S>) -> Unit): List<S> {
    while (state.itemsToProcess.isNotEmpty()) {
        val item = state.itemsToProcess.removeFirst()
        process(item, state)
    }
    return state.result
}