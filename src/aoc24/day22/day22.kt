package day22

import println
import readInputResources

private const val DAY_NAME = "2024/Day22"


fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:").apply {
        check(this == 13764677935L)
    }
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    return input.sumOf {
        getSecretNumbers(it.toLong(), 2000).last()
    }
}

private fun checkPart1() {
    val testNumber = 123L
    val nextTenRandomsForTestNumber = listOf(
        15887950L,
        16495136L,
        527345L,
        704524L,
        1553684L,
        12683156L,
        11100544L,
        12249484L,
        7753432L,
        5908254L,
    )

    (1..10).forEach {
        "Iteration $it".println()

        val result = getSecretNumbers(testNumber, it).last()
        println("Result for $it is $result ${java.lang.Long.toBinaryString(result)}")
        check(result == nextTenRandomsForTestNumber[it - 1])
    }


    val expectedResult = mapOf(
        1 to 8685429,
        10 to 4700978,
        100 to 15273692,
        2024 to 8667524,
    )
    expectedResult.forEach { (number, expected) ->
        "Value $number".println()

        val result = getSecretNumbers(number.toLong(), 2000).last()
        println("Result for $number is $result ${java.lang.Long.toBinaryString(result)}")
        check(result == expected.toLong())
    }


    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 37327623L)
}

fun getSecretNumbers(testNumber: Long, it: Int) = sequence<Long> {
    var secretNumber = testNumber
    repeat(it) {
        secretNumber = (secretNumber.times(64)).xor(secretNumber)
        secretNumber = secretNumber.mod(16777216L)


        secretNumber = secretNumber.div(32).xor(secretNumber)
        secretNumber = secretNumber.mod(16777216L)



        secretNumber = secretNumber.times(2048).xor(secretNumber)
        secretNumber = secretNumber.mod(16777216L)
        yield(secretNumber)

    }
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test2")).println("Part two test result") == 23L)
}

private fun part2(input: List<String>): Long {
    val result = input.flatMapIndexed { index: Int, it: String ->
        getSecretNumbers(it.toLong(), 2000)
            .map {
                it.mod(10)
            }
            .windowed(5)
            .mapIndexed { index, it ->
                listOf(
                    it[1] - it[0],
                    it[2] - it[1],
                    it[3] - it[2],
                    it[4] - it[3]
                ).joinToString("|") to (it.last() to index)
            }.groupBy({ it.first }) { it.second }
            .mapValues {
                it.value.minBy { it.second }
            }.entries
    }.groupBy({ it.key }) { it.value.first }
//        .mapValues { it.value.sum() }
        .maxBy { it.value.sum() }
    result.println("Result ")
    return result.value.sum().toLong()
}


