import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

private const val DAY_NAME = "2024/Day14"
fun main() {
    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


data class Robot(
    val id: Int = idx++,
    val initialPosition: Position,
    val movement: Position,
    var currentPosition: Position = initialPosition
)

val regex = """p=(?<xposition>-?\d+),(?<yposition>-?\d+) v=(?<xmove>-?\d+),(?<ymove>-?\d+)""".toRegex()
private fun part1(input: List<String>, columnCount: Int = 101, rowsCount: Int = 103, steps: Int = 100): Long {
    val middleRow = rowsCount / 2
    val midleColumn = columnCount / 2
    println("Middle row: $middleRow, Middle column: $midleColumn")
    val robots = parseRobot(input)


    val movedRobots = moveRobots(robots, steps, rowsCount, columnCount)
    movedRobots.forEach {
        it.currentPosition.println("Robot ${it.id}")
    }
    return movedRobots.filter {
        it.currentPosition.row != middleRow.toLong() && it.currentPosition.column !== midleColumn.toLong()

    }.partition {
        it.currentPosition.row <= rowsCount / 2
    }.let {
        it.toList().flatMap { robots ->
            robots.partition {
                it.currentPosition.column <= columnCount / 2
            }.toList()
        }

    }.map {
        it.count().println("Squares ${it.map { it.currentPosition }}")
    }.reduce { acc, i -> acc * i }.toLong()


}

private fun parseRobot(input: List<String>): List<Robot> {
    val robots = input.map {
        it.println()
        val (xposition, yposition, xmove, ymove) = regex.find(it)!!.destructured
        Robot(
            initialPosition = Position(column = xposition.toInt(), row = yposition.toInt()),
            movement = Position(column = xmove.toInt(), row = ymove.toInt())
        )
    }.println("Robots")
    return robots
}

private fun moveRobots(
    robots: List<Robot>,
    steps: Int,
    rowsCount: Int,
    columnCount: Int
): List<Robot> {
    val movedRobots = robots.map {
        val result = Position(
            row = it.initialPosition.row,
            column = it.initialPosition.column
        ).move(it.movement.multiply(steps.toLong()))


        it.currentPosition = Position(row = result.row.mod(rowsCount), column = result.column.mod(columnCount))
        it
    }
    return movedRobots
}

private fun checkPart1() {

    val testParametr: (Int, Robot) -> List<Robot> = { step, robot ->
        moveRobots(
            robots = listOf(
                robot
            ),
            steps = step,
            rowsCount = 7,
            columnCount = 11,

            )

    }
    check(
        parseRobot(listOf("p=10,3 v=-1,2")) == listOf(
            Robot(
                id = 0,
                initialPosition = Position(column = 10, row = 3),
                movement = Position(column = -1, row = 2)
            )
        )
    )


    val robot =
        Robot(id = 0, initialPosition = Position(column = 2, row = 4), movement = Position(column = 2, row = -3))




    check(
        testParametr(1, robot).println("Movement result 1 steps") == listOf(
            robot.copy(
                currentPosition = Position(
                    column = 4,
                    row = 1
                )
            )
        )
    )
    check(
        testParametr(2, robot).println("Movement result 2 steps") == listOf(
            robot.copy(
                currentPosition = Position(
                    column = 6,
                    row = 5
                )
            )
        )
    )
    check(
        testParametr(3, robot).println("Movement result 3 steps") == listOf(
            robot.copy(
                currentPosition = Position(
                    column = 8,
                    row = 2
                )
            )
        )
    )
    check(
        testParametr(5, robot).println("Movement result 5 steps") == listOf(
            robot.copy(
                currentPosition = Position(
                    column = 1,
                    row = 3
                )
            )
        )
    )


    val p12Robot =
        Robot(id = 12, initialPosition = Position(column = 9, row = 5), movement = Position(column = -3, row = -3))
    check(
        testParametr(
            1,
            p12Robot
        ).println("[12] Movement result 1 steps") == listOf(
            p12Robot.copy(
                currentPosition = Position(
                    column = 6,
                    row = 2
                )
            )
        )
    )
    check(
        testParametr(
            2,
            p12Robot
        ).println("[12] Movement result 2 steps") == listOf(
            p12Robot.copy(
                currentPosition = Position(
                    column = 3,
                    row = 6
                )
            )
        )
    )
    check(
        testParametr(
            3,
            p12Robot
        ).println("[12] Movement result 3 steps") == listOf(
            p12Robot.copy(
                currentPosition = Position(
                    column = 0,
                    row = 3
                )
            )
        )
    )
    check(
        testParametr(
            100,
            p12Robot
        ).println("[12] Movement result 3 steps") == listOf(
            p12Robot.copy(
                currentPosition = Position(
                    column = 6,
                    row = 6
                )
            )
        )
    )




    check(part1(readInputResources(DAY_NAME, "test"), 11, 7).println("Part one test result") == 12L)
    check(part1(readInputResources(DAY_NAME, "input")).println("Part one test result") < 241382700L)
}


private fun part2(input: List<String>, columnCount: Int = 101, rowsCount: Int = 103): Long {
    var robots = parseRobot(input)
    var secondsRange = 0..<9587
    var maxGroup = 0
    var maxGroupSecond = 0
    for (seconds in secondsRange) {
        robots = moveRobots(robots, seconds, columnCount = columnCount, rowsCount = rowsCount)


        val positionedRobots = robots.associateBy { it.currentPosition }
        val toVisit =
            positionedRobots.keys.toMutableList().sortedWith(compareBy({ it.row }, { it.column })).toMutableList()
        val groups: MutableMap<Position, MutableSet<Position>> = mutableMapOf()
        while (toVisit.isNotEmpty()) {
            val position: Position = toVisit.removeFirst()
            val neighbours = position.allAround()
            (neighbours.values + listOf(
                position.northEast(),
                position.northWest(),
                position.southEast(),
                position.southWest()
            )).forEach {
                if (it in positionedRobots.keys) {

                    groups.getOrPut(position, { mutableSetOf(position) }).add(it)

                    groups[it]?.let {
                        groups[position]!!.addAll(it)
                        for (inner in it) {
                            groups[inner] = groups[position]!!
                        }
                    }


                }
            }

        }
        //  > 4334
        //
        val maxGroupSize = groups.maxBy { it.value.size }.value.size

        maxGroupSize.println("Max group size at sec $seconds")
        if (maxGroupSize > maxGroup) {

            maxGroup = maxGroupSize.println("New max group size at sec $seconds")
            maxGroupSecond = seconds
        }

//        printImage(columnCount, rowsCount, positionedRobots, seconds)


    }

    File("result.txt").writeText(
        (0..<rowsCount).map { row ->
            (0..<columnCount).map { column ->
                val result =
                    moveRobots(
                        robots,
                        maxGroupSecond,
                        rowsCount,
                        columnCount,

                        ).associate { it.currentPosition to it }
                printImage(columnCount, rowsCount, result, maxGroupSecond)
                val position = Position(column = column, row = row)
                if (position in result.keys) {
                    "#"
                } else {
                    "."
                }
            }.joinToString("")
        }.joinToString("\n")
    )


    return maxGroupSecond.toLong()

}

private fun printImage(
    columnCount: Int,
    rowsCount: Int,
    positionedRobots: Map<Position, Robot>,
    seconds: Int
) {
    val image = BufferedImage(columnCount, rowsCount, BufferedImage.TYPE_INT_RGB)

    for (row in 0 until rowsCount) {
        for (column in 0 until columnCount) {
            val position = Position(column = column, row = row)
            if (position in positionedRobots.keys) {
                image.setRGB(column, row, Color.WHITE.rgb)
            } else {
                image.setRGB(column, row, Color.BLACK.rgb)
            }
        }
    }

    ImageIO.write(image, "png", File("data3/${seconds}_result.png"))
}





