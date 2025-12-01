private const val DAY_NAME = "2024/Day03"
fun main() {
//    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


val mulRegex = "mul\\((?<X>\\d{1,3}),(?<Y>\\d{1,3})\\)".toRegex()
private fun part1(input: List<String>): Long {
    return input.sumOf {
        mulRegex.findAll(it)
            .map { matchResult ->

                (matchResult.groups.get("X")!!.value.toLong() * matchResult.groups.get("Y")!!.value.toLong()).println("Result of : ${matchResult.value}")
            }
            .sum()
    }
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 161L)
    check(part1(readInputResources(DAY_NAME, "input")).println("Part one test result") != 32280404L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test2")).println("Part two test result") == 48L)
    check(part2(readInputResources(DAY_NAME, "input")).println("Part two test result") != 108108906L)
}


val doDontRegex = "(do\\(\\)|don't\\(\\))".toRegex()
private fun part2(input: List<String>): Long {

    var doingStuff = true
    return input.mapIndexed { index, it ->
        "[processing lane][${index}]".println()
        val donts = doDontRegex.findAll(it).map {
            it.range.first to (it.value == "do()")
        }.toMap()


        val sum = mulRegex.findAll(it)
            .mapNotNull { matchResult ->
                val keyOfLast = donts.keys.filter { it < matchResult.range.first }.maxOrNull()
                doingStuff =
                    donts.getOrDefault(keyOfLast, doingStuff)
                matchResult.value.println("donts? ${doingStuff} keyOfLastCommand ${keyOfLast} index=${matchResult.range}")
                if (doingStuff)
                    (matchResult.groups.get("X")!!.value.toLong() * matchResult.groups.get("Y")!!.value.toLong()).println(
                        "Result of : ${matchResult.value}"
                    )
                else null
            }
            .sum()


        doingStuff = donts[donts.keys.max()]!!
        "[processing lane][${index}] finish $doingStuff ".println()
        sum
    }.sum()
}


