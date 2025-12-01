private const val DAY_NAME = "2024/Day07"
fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    return input.sumOf {
        val (testValueString, numbersString) = it.println("xxx").split(":")
        val testValue = testValueString.toLong()
        val numbers = numbersString.trim().split(" ").map { it.toLong() }

        if( checkNumbers(numbers, Operation.SUM, testValue) || checkNumbers(numbers, Operation.MULTIPLY, testValue))
            testValue
        else
            0
    }
}

enum class Operation {
    SUM, MULTIPLY, COMBINE
}
private fun checkNumbers(numbers: List<Long>, operation: Operation , testValue : Long, accum: Long=0, index:Int=0, includeCombine :Boolean = false): Boolean {
    if (index == numbers.size) {
        return accum == testValue
    }
    if(accum > testValue) return false

    return when(operation) {
        Operation.SUM -> Operation.values().any {  checkNumbers(numbers, it, testValue, accum + numbers[index], index + 1, includeCombine) }
        Operation.MULTIPLY ->  Operation.values().any { checkNumbers(numbers, it, testValue, accum * numbers[index], index + 1, includeCombine) }

        Operation.COMBINE ->  includeCombine && Operation.values().any { checkNumbers(numbers, it, testValue, (accum.toString() + numbers[index].toString()).toLong(), index + 1, includeCombine) }
    }
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 3749L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 11387L)
}

private fun part2(input: List<String>): Long {

    return input.sumOf {
        val (testValueString, numbersString) = it.println("xxx").split(":")
        val testValue = testValueString.toLong()
        val numbers = numbersString.trim().split(" ").map { it.toLong() }

        if( Operation.values().any {  checkNumbers(numbers, it, testValue, includeCombine = true)})
            testValue
        else
            0
    }
}


