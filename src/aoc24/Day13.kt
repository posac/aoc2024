private const val DAY_NAME = "2024/Day13"
fun main() {
//    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}




private fun part1(input: List<String>):Long {
    return input.filter { it.isNotEmpty() }.chunked(3).map {
        val buttonAString = it[0]
        val buttonBString = it[1]
        val priceString = it[2]

        val (ax , ay) = "\\d+".toRegex().findAll(buttonAString).flatMap { it.groups }.map { it!!.value.toBigDecimal() }.toList()
        val (bx , by) = "\\d+".toRegex().findAll(buttonBString).flatMap { it.groups }.map { it!!.value.toBigDecimal() }.toList()
        val (px , py) = "\\d+".toRegex().findAll(priceString).flatMap { it.groups }.map { it!!.value.toBigDecimal() }.toList()


        try {
            val xsolution = (px.multiply(by) - py.multiply(bx)).divide(ax.multiply(by).minus(ay.multiply(bx)))
            val ysolution = (px - ax * xsolution).divide(bx)
            xsolution.println("X solution")
            ysolution.println("X solution")
            (3*xsolution.toLong() + ysolution.toLong()).println("Solution")
        } catch (e: ArithmeticException) {
            println("No solution")
            0L
        }

    }.sum().println("Sum")


}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 480L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 281L)
}

private fun part2(input: List<String>): Long {
    return input.filter { it.isNotEmpty() }.chunked(3).map {
        val buttonAString = it[0]
        val buttonBString = it[1]
        val priceString = it[2]

        val (ax , ay) = "\\d+".toRegex().findAll(buttonAString).flatMap { it.groups }.map { it!!.value.toBigDecimal() }.toList()
        val (bx , by) = "\\d+".toRegex().findAll(buttonBString).flatMap { it.groups }.map { it!!.value.toBigDecimal() }.toList()
        var (px , py) = "\\d+".toRegex().findAll(priceString).flatMap { it.groups }.map { it!!.value.toBigDecimal() }.toList()
        px= px +10000000000000.toBigDecimal()
        py= py +10000000000000.toBigDecimal()


        try {
            val xsolution = (px.multiply(by) - py.multiply(bx)).divide(ax.multiply(by).minus(ay.multiply(bx)))
            val ysolution = (px - ax * xsolution).divide(bx)
            xsolution.println("X solution")
            ysolution.println("X solution")
            (3*xsolution.toLong() + ysolution.toLong()).println("Solution")
        } catch (e: ArithmeticException) {
            println("No solution")
            0L
        }

    }.sum().println("Sum")

}


