import kotlin.math.abs
import kotlin.math.sign

private const val DAY_NAME = "Day02"
fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    return parse(input).map {
        it.zipWithNext { prev, next ->
            next - prev
        }
    }.filter {
        val increasing = it.all { it > 0 }
        val decreasing = it.all { it < 0 }
        it.all { abs(it) <= 3 } && (increasing || decreasing)

    }.size.toLong()
}

private fun parse(input: List<String>) = input.map {
    it.split("\\s+".toRegex())
        .map {
            it.toLong()
        }


}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 2L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 4L)
    check(part2(readInputResources(DAY_NAME, "input")).println("Part two test result") > 351L)
}

private fun part2(input: List<String>): Long = parse(input).map {
    it.zipWithNext { prev, next ->
        next - prev
    }
}.filterIndexed { index, it ->
    println("Processing => ${it} ${index}")
    val (increasing, decreasing, levelDiffs) = calculateSafetyStatiscits(it)
    val stable = it.count { it == 0L }
    val xx = setOf(1, 2)
    if ((levelDiffs != it.size && (levelDiffs >= it.size - 2)) || (xx.contains(increasing) || xx.contains(decreasing) || stable ==1)) {
        val index: Int
        val broken: Long?
        if(stable ==1){
            println("stable breach")
            broken = it.find { it == 0L }
            index = it.indexOf(broken)
        } else if (xx.contains(decreasing)) {
            println("decreasing breach")
            broken = it.find { it < 0 }
            index = it.indexOf(broken)

        } else if (xx.contains(increasing)) {
            println("increasing breach")
            broken = it.find { it > 0 }
            index = it.indexOf(broken)

        } else {
            println("Thhreshold breach")
            broken = it.find { !setOf(1, 2, 3L).contains(it) }
            index = it.indexOf(broken)
        }
        println("index broken ${index} broken ${broken}")
        val fixedReport = it.toMutableList()
        if (index < it.size - 1) {
            println("Here")
            fixedReport[index] = fixedReport[index + 1] + fixedReport[index]
            fixedReport.removeAt(index + 1)
        } else {
            fixedReport.removeAt(index)
        }
        fixedReport.println("Fixed report")

        val (increasing, decreasing, levelDiffs) = calculateSafetyStatiscits(fixedReport)
        isSafety(levelDiffs, fixedReport, increasing, decreasing)


    } else isSafety(levelDiffs, it, increasing, decreasing)


}.size.toLong()

private fun isSafety(
    levelDiffs: Int,
    it: List<Long>,
    increasing: Int,
    decreasing: Int
) = levelDiffs == it.size && (increasing == it.size || decreasing == it.size)

private fun calculateSafetyStatiscits(it: List<Long>): Triple<Int, Int, Int> {
    val increasing = it.count { it > 0 }
    val decreasing = it.count { it < 0 }
    val levelDiffs = it.count { setOf(1, 2, 3L).contains(abs(it)) }
    return Triple(increasing, decreasing, levelDiffs)
}


