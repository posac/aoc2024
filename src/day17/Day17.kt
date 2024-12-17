package day17

import println
import readInputResources
import java.util.concurrent.ConcurrentHashMap

private const val DAY_NAME = "Day17"

data class Registers(
    var a: Long = 0,
    var b: Long = 0,
    var c: Long = 0,
    val out: MutableList<Long> = mutableListOf(),
    val expectedOut: List<Long>? = null
) {

    fun output() = out.joinToString(",")
}

enum class Opcodes(val code: Long) {
    ADV(0),
    BXL(1),
    BST(2),
    JNZ(3),
    BXC(4),
    OUT(5),
    BDV(6),
    CDV(7);

    companion object {
        fun fromInt(code: Long) = values().first { it.code == code }
    }

    fun getLiteralOperand() = code

    fun getComboOperand(registers: Registers) = when (this) {
        ADV -> code
        BXL -> code
        BST -> code
        JNZ -> code
        BXC -> registers.a
        OUT -> registers.b
        BDV -> registers.c
        CDV -> throw IllegalArgumentException("CDV does not have literal operand")

    }
}


fun main() {
//    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
//    part1(input).println("Part one result:")
    part2(input, startingPoint = 500_000_000, endingPoint = Long.MAX_VALUE).apply {
        this.println("Part two result:")
        check(this > 100_000_000L)
        check(this > 500_000_000L)
    }
}


private fun part1(input: List<String>): String {
    val registers = Registers(
        a = input[0].split(": ")[1].toLong(),
        b = input[1].split(": ")[1].toLong(),
        c = input[2].split(": ")[1].toLong(),
    ).println("Registers")


    execute(input[4].split(": ")[1], registers)
    return registers.output()
}

private fun execute(input: String, registers: Registers): Registers {
    val program = input.split(",").map { Opcodes.fromInt(it.toLong()) }

    var instructionPointer = 0
    while (program.size > instructionPointer) {
        val instruction = program[instructionPointer]!!
        val comboOperant = program[instructionPointer + 1]!!
        when (instruction) {
            Opcodes.ADV -> {

                registers.a = registers.a / Math.pow(2.0, comboOperant.getComboOperand(registers).toDouble()).toInt()
                instructionPointer += 2
            }

            Opcodes.BXL -> {
                registers.b = registers.b xor comboOperant.getLiteralOperand()
                instructionPointer += 2
            }

            Opcodes.BST -> {
                registers.b = comboOperant.getComboOperand(registers).mod(8L)
                instructionPointer += 2
            }

            Opcodes.JNZ -> {
                if (registers.a != 0L) {
                    instructionPointer = comboOperant.getComboOperand(registers).toInt()
                } else
                    instructionPointer += 2
            }

            Opcodes.BXC -> {
                registers.b = registers.b xor registers.c
                instructionPointer += 2
            }

            Opcodes.OUT -> {
                val element = comboOperant.getComboOperand(registers).mod(8L)
                if (registers.expectedOut != null) {
                    if (registers.expectedOut[registers.out.size] != element)
                        return registers
                }
                registers.out.add(element)
                instructionPointer += 2
            }

            Opcodes.BDV -> {
                registers.b = registers.a / Math.pow(2.0, comboOperant.getComboOperand(registers).toDouble()).toInt()
                instructionPointer += 2
            }

            Opcodes.CDV -> {
                registers.c = registers.a / Math.pow(2.0, comboOperant.getComboOperand(registers).toDouble()).toInt()
                instructionPointer += 2
            }

            else -> throw IllegalArgumentException("Unknown instruction: $instruction")
        }


    }
    registers
//        .println("Result: ")
    return registers
}

private fun checkPart1() {
    execute("2,6", Registers(c = 9)).apply {
        check(this.b == 1L)
    }


    execute("5,0,5,1,5,4", Registers(a = 10)).apply {
        check(this.out == listOf(0L, 1, 2))
    }

    execute("0,1,5,4,3,0", Registers(a = 2024)).apply {
        check(this.output() == "4,2,5,6,7,7,7,7,3,1,0")
        check(this.a == 0L)
    }

    execute("1,7", Registers(b = 29)).apply {
        check(this.b == 26L)
    }

    execute("4,0", Registers(b = 2024, c = 43690)).apply {
        check(this.b == 44354L)
    }


    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == "4,6,3,5,6,3,5,2,1,0")
    part1(readInputResources(DAY_NAME, "input")).println("Part one test result").apply {
        check(this != "0,0,5,1,7,2,3,4,3")
        check(this == "1,5,7,4,1,6,0,3,0")
    }


}

private fun checkPart2() {
//    check(part2(readInputResources(DAY_NAME, "test2")).println("Part two test result") == 117440L)
}


private fun part2(input: List<String>, startingPoint: Long = 0, endingPoint: Long = 1_000_000_000): Long {
    val registers = Registers(
        a = input[0].split(": ")[1].toLong(),
        b = input[1].split(": ")[1].toLong(),
        c = input[2].split(": ")[1].toLong(),
        expectedOut = input[4].split(": ")[1].split(",").map { it.toLong() }
    ).println("Registers")
    val program = input[4].split(": ")[1].println("expected").split(",").map { Opcodes.fromInt(it.toLong()) }


    val cache = ConcurrentHashMap<ItemKey, List<Long>>()
    val count = 4
//    println(Integer.toOctalString(5714775))
//    var current = Math.pow(8.toDouble(), count.toDouble()).toLong()  + 1546749L
    var current = 78549609847293
//    var current = 509L
    var part2Result = -1L
//    LongRange(1L, 20048L)
    LongRange(current, current * 8)
        .forEach { newARegistry ->

//                val value = async {
            val a = current  // java.lang.Long.parseLong("${newARegistry}20", 8)
            val result = execute(
                input = input[4].split(": ")[1],
                registers = registers.copy(a = a, out = mutableListOf())
            )
            if (result.out.size > 8)
                println("New A Registry ${result.output()} ${java.lang.Long.toOctalString(a)} $a")

            if (result.out == registers.expectedOut) {
                println("Found: $newARegistry")
                part2Result = a
                return part2Result
            }
            current += 4194304
//            4194304
//        current+=1024
//        current+=64
//        current+=1

        }
    return part2Result
}


data class ItemKey(val a: Long, val b: Long, val c: Long, val pointer: Int)


