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
    check(part2(readInputResources(DAY_NAME, "test_mp")).println("Part two test_mp result") == 2L)
    val result2 = part2(readInputResources(DAY_NAME, "input")).println("Part two test result")
    check( result2 > 351L)
    check( result2 < 412L)
}

private fun part2(input: List<String>): Long = parse(input).filter {
    checkList(it).println("Results for list ${it}")
}.size.toLong()

private fun checkList(list: List<Long>, increasing: Boolean? = null, thresholdProblem: Int = 1, iteration : Int = 0): Boolean {
    println("[checkList][$iteration] increasing=$increasing thresholdProblem=$thresholdProblem list=$list")
    if (list.size < 2)
        return true
    if (list.size == 2) {
        val first = list[0]
        val second = list[1]
        val distance = second - first

        return if (increasing == null || thresholdProblem != 0) true else (if (increasing) distance > 0 else distance < 0)
    }
    val first = list[0]
    val second = list[1]
    val third = list[2]
    val distanceOneTwo = second - first
    val distanceTwoThree = third - second

    var newIcreasing = calculateIncreasing(increasing, distanceOneTwo)

    if(iteration == 0 &&!setOf(1, 2, 3L).contains(abs(distanceOneTwo))){
        return checkList(
            listOf(first) + list.drop(2),
            increasing,
            thresholdProblem - 1,
            iteration=iteration+1
        ) || checkList(listOf(second) + list.drop(2), increasing, thresholdProblem - 1,iteration=iteration+1)
    } else if (  !setOf(1, 2, 3L).contains(abs(distanceTwoThree))) {
        if (thresholdProblem != 0)
            return checkList(
                listOf(first,second) + list.drop(3),
                increasing,
                thresholdProblem - 1,
                iteration=iteration+1
            ) || checkList(listOf(first) + list.drop(2), increasing, thresholdProblem - 1,iteration=iteration+1)
        else return false
    } else
        return checkList(list.drop(1), newIcreasing, thresholdProblem,iteration=iteration+1)

}

private fun calculateIncreasing(increasing: Boolean?, distanceOneTwo: Long): Boolean? {
    var newIcreasing = increasing
    if (newIcreasing == null) {
        newIcreasing = if (distanceOneTwo > 0) {
            true
        } else if (distanceOneTwo < 0) {
            false
        } else null
    }
    return newIcreasing
}

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


