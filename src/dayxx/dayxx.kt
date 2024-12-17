package dayxx

import println
import readInputResources

private const val DAY_NAME = "Day01"

data class Registers(var a: Int, val b: Int, val c: Int, val out: MutableList<Int> = mutableListOf())
enum class Opcodes(val code: Int) {
    ADV(0),
    BXL(1),
    BST(2),
    JNZ(3),
    BXC(4),
    OUT(5),
    BDV(6),
    CDV(7),
}


fun main() {
    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): String {
    val registers = Registers(
        a=input[0].split(": ")[1].toInt(),
        b=input[1].split(": ")[1].toInt(),
        c=input[2].split(": ")[1].toInt(),
    ).println("Registers")



    return input.size.toLong().toString()
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == "4,6,3,5,6,3,5,2,1,0")
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 281L)
}

private fun part2(input: List<String>): Long = input.size.toLong()


