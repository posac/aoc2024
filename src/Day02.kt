import kotlin.math.abs

private const val DAY_NAME = "Day02"
fun main() {
    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    return parse(input).filter {
        checkList(it, thresholdProblem = 0).println("Results for list ${it}")
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
    check(part1(readInputResources(DAY_NAME, "input")).println("Part one result") == 314L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test_mp")).println("Part two test_mp result") == 2L)
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 4L)
    val result2 = part2(readInputResources(DAY_NAME, "input")).println("Part two test result")
    check(result2 > 351L)
    check(result2 != 408L)
    check(result2 != 407L)
    check(result2 < 412L)
    check(result2 == 373L)
}

private fun part2(input: List<String>): Long = parse(input).filter {
    checkList(it).println("Results for list ${it}")
}.size.toLong()

private fun checkList(
    list: List<Long>,
    increasing: Boolean? = null,
    thresholdProblem: Int = 1,
    iteration: Int = 0
): Boolean {
    println("[checkList][$iteration] increasing=$increasing thresholdProblem=$thresholdProblem list=$list")
    if (list.size < 2)
        return true
    if (list.size == 2) {
        val first = list[0]
        val second = list[1]
        val distance = second - first

        return if (increasing == null || thresholdProblem != 0)
            true
        else ((if (increasing) distance > 0 else distance < 0) && isValidDistance(
            distance
        ))
    }
    val first = list[0]
    val second = list[1]
    val third = list[2]
    val distanceOneTwo = second - first
    val distanceTwoThree = third - second

    val newIcreasing = calculateIncreasing(increasing, distanceOneTwo)
    val firstPairIncreasing = distanceOneTwo > 0
    val secondPairIncreasing = distanceTwoThree > 0

    if (iteration == 0 && (!isValidDistance(distanceOneTwo) || !isValidIncrease(
            firstPairIncreasing,
            secondPairIncreasing,
            increasing
        ))
    ) {

        if (thresholdProblem != 0) {
            return checkList(
                list = listOf(first) + list.drop(2),
                increasing = increasing,
                thresholdProblem = thresholdProblem - 1,
                iteration = iteration
            ) || checkList(
                list = listOf(second) + list.drop(2),
                increasing = increasing,
                thresholdProblem = thresholdProblem - 1,
                iteration = iteration
            )
        } else return false
    } else if ((!isValidDistance(distanceOneTwo) || !isValidDistance(distanceTwoThree) || !isValidIncrease(
            firstPairIncreasing,
            secondPairIncreasing,
            increasing
        ))
    ) {
        if (thresholdProblem != 0)
            return checkList(
                list = listOf(first, second) + list.drop(3),
                increasing = newIcreasing,
                thresholdProblem = thresholdProblem - 1,
                iteration = iteration + 1
            ) || checkList(
                list = listOf(first) + list.drop(2),
                increasing = newIcreasing,
                thresholdProblem = thresholdProblem - 1,
                iteration = iteration + 1
            )
        else return false
    } else
        return checkList(
            list = list.drop(1),
            increasing = newIcreasing,
            thresholdProblem = thresholdProblem,
            iteration = iteration + 1
        )

}

private fun isValidIncrease(firstPair: Boolean, secondPair: Boolean, increasing: Boolean?) =
    if (increasing == null) firstPair == secondPair else (secondPair == increasing) && (firstPair == increasing)

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

val VALID_DISTANCE = setOf(1, 2, 3L)
private fun isValidDistance(distanceOneTwo: Long) = VALID_DISTANCE.contains(abs(distanceOneTwo))


