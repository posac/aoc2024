package day24

import println
import readInputResources
import kotlin.math.max

private const val DAY_NAME = "Day24"


fun main() {
//    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
//    part1(input).println("Part one result:")
    part2(input).println("Part two result:")

//    displayNodes(
//        Node(label = "test", left = Node(label = "left",left = Node(label = "leftleft"    ), right = Node(label = "leftright")    ), right = Node(label = "right"))
//    )
}

enum class Operation {
    AND, OR, XOR
}

data class Operations(
    val leftLabel: String,
    val rightLabel: String,
    val operation: Operation,
    val outputLabel: String
) {
    var calculated: Boolean = false
}

private fun part1(input: List<String>): Long {
    val (state, operations) = parseGame(input)

    return processState(state, operations)
}

private fun parseGame(input: List<String>): Pair<MutableMap<String, Boolean>, List<Operations>> {
    val state = input.takeWhile { it.isNotEmpty() }.map {
        val (label, initialState) = it.split(": ")
        label to (initialState == "1")
    }.toMap().toMutableMap()
    val operations = input.dropWhile { it.isNotEmpty() }.drop(1).map {
        val (left, operation, right, operator) = """(\w+) (\w+) (\w+) -> (\w+)""".toRegex().find(it)!!.destructured
        Operations(
            leftLabel = left,
            rightLabel = right,
            operation = Operation.valueOf(operation),
            outputLabel = operator
        )
    }
    return Pair(state, operations)
}

private fun processState(
    intialState: Map<String, Boolean>,
    allOperations: List<Operations>
): Long {
    val state = intialState.toMutableMap()
    val operations = allOperations.map { it.copy() }.toMutableList()

    while (operations.any { !it.calculated }) {
        for (operation in operations) {
            if (operation.calculated) continue
            if (operation.leftLabel !in state || operation.rightLabel !in state)
                continue
            val left = state[operation.leftLabel]!!
            val right = state[operation.rightLabel]!!

            val result = when (operation.operation) {
                Operation.AND -> left && right
                Operation.OR -> left || right
                Operation.XOR -> left xor right
            }
//            println("[${operation.outputLabel}]$left ${operation.operation}  $right = $result")
            operation.calculated = true
            state[operation.outputLabel] = result
        }
    }


    val binaryString = state.entries.groupBy({ it.key.takeWhile { !it.isDigit() } }).mapValues {
        it.value.sortedByDescending { it.key.dropWhile { !it.isDigit() }.toInt() }
            .map { if (it.value) "1" else "0" }
            .joinToString("")
    }

//    binaryString.forEach { t, u ->
//        println("$t : $u")
//    }


    return java.lang.Long.parseLong(
        binaryString["z"], 2
    )
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test_small")).println("Part one test result") == 4L)
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 2024L)

}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 281L)

}

data class Node(val label: String, var left: Node? = null, var right: Node? = null, val operation: Operations? = null)

private fun part2(input: List<String>): Long {
    var (state, operations) = parseGame(input)

    val swapItems = mapOf(
        //first
        "z08" to "ffj",
        "ffj" to "z8",

        // second
        "kfm" to "dwp",
        "dwp" to "kfm",

        //third
        "z22" to "gjh",
        "gjh" to "z22",

        // fourth
        "jdr" to "z31",
        "z31" to "jdr",

        )
    operations = operations.map {
        if (it.outputLabel in swapItems) {
            it.copy(outputLabel = swapItems[it.outputLabel]!!)
        } else
            it
    }
//    val expectedResult = state.entries.groupBy {
//        it.key.first()
//    }.mapValues {
//        it.value.sortedByDescending { it.key.drop(1).toInt() }.map { if (it.value) "1" else "0" }.joinToString("")
//    }
//        .println("Binnary")
//        .mapValues { java.lang.Long.parseLong(it.value, 2) }
//        .println("Longs: ")
//        .values.sum().println("Expected result")
//
//    val actualResult = processState(state, operations).println("Result2")
//
//    val expectedResultbinary = java.lang.Long.toBinaryString(expectedResult).println("E:")
//    val actualResultbinary = java.lang.Long.toBinaryString(actualResult).println("A:")
//
//    val indexesWrong =
//        expectedResultbinary.zip(actualResultbinary).mapIndexed { index, (e, a) -> if (e != a) index else -1 }
//            .filter { it != -1 }
//    indexesWrong.println("Indexes wrong")
//
    (0..45).forEach { number ->

        val expected = Math.pow(2.0, number.toDouble()).toLong()
        val result = java.lang.Long.toBinaryString(expected).reversed()
        val xAdd = (0..44).flatMap {
            listOf("x%02d".format(it) to (result.getOrElse(it) { '0' } == '1'),
                "y%02d".format(it) to false)

        }.toMap()
        val yAdd = (0..44).flatMap {
            listOf(
                "y%02d".format(it) to (result.getOrElse(it) { '0' } == '1'),
                "x%02d".format(it) to false)

        }.toMap()
        val addboth = (0..44).flatMap {
            listOf(
                "y%02d".format(it) to (result.getOrElse(it) { '0' } == '1'),
                "x%02d".format(it) to (result.getOrElse(it) { '0' } == '1'),
            )

        }.toMap()

        val xResult = processState(xAdd, operations.map { it.copy() })
        val yResult = processState(yAdd, operations.map { it.copy() })
        val bothResult = processState(addboth, operations.map { it.copy() })

        check(xResult == yResult) { "X: $xResult Y: $yResult when adding=$expected+0" }
//        check(xResult == expected.toLong()) { "X: $xResult expected: $expected when adding=$expected+0 bit $number" }
//        check(bothResult == 2 * expected.toLong()) { "X: $bothResult expected: ${2 * expected} when adding=$expected+${expected} bit $number" }

    }

    swapItems.keys.sorted().joinToString(",").println("result:")
    return -1L
}

fun displayNodes(node: Node): List<String> {

    val leftString = node.left?.let { displayNodes(it) }
    val rightString = node.right?.let { displayNodes(it) }
    val leftLengh = leftString?.first()?.length ?: 0
    val rightLengh = rightString?.first()?.length ?: 0
    val sum = (0..<max(leftString?.size ?: 0, rightString?.size ?: 0)).map {
        val left = leftString?.getOrNull(it) ?: " ".repeat(leftLengh ?: 10)
        val right = rightString?.getOrNull(it) ?: " ".repeat(rightLengh ?: 10)
        listOfNotNull(left, right).joinToString("|${node.operation?.operation}| ")
    }
    return listOf("-".repeat(leftLengh ?: 5) + node.label + "-".repeat(rightLengh ?: 5)) + sum
}


