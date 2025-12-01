private const val DAY_NAME = "2024/Day12"
fun main() {
//    checkPart1()
    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


var idx = 0

enum class GardenState {
    OUTSIDE_GAME,
    IN_GARDEN,
    OUT_GARDEN,
}

data class Garden(
    val position: MutableSet<Position>,
    val gardenValue: Char,
    val id: Int = idx++,
    val game: Map<Position, Char>
) {
    fun mergeGarden(garden: Garden) {
        position.addAll(garden.position)
    }

    fun state(position: Position): GardenState {
        return when {
//            position !in game.keys -> GardenState.OUTSIDE_GAME
            position in this.position -> GardenState.IN_GARDEN
            else -> GardenState.OUT_GARDEN
        }
    }
}

private fun part1(input: List<String>): Long {
    val game = parsePositionGame(input) { it }

    val gardens = game.mapValues { Garden(mutableSetOf(it.key), it.value, game = game) }.toMutableMap()
    for (position in game.keys.sortedWith(compareBy({ it.row }, { it.column }))) {
        val neighbours = position.allAround()
        val gardenValue = game[position]
        var foundGarden: Garden = gardens[position]!!
        neighbours.forEach {
            if (it.value in game.keys && game[it.value] == gardenValue) {
                foundGarden.mergeGarden(gardens[it.value]!!)
                foundGarden.position.forEach {
                    gardens[it] = foundGarden
                }

            }
        }
    }



    return gardens.values.toSet().sumOf { garden ->
        val perimeter = garden.position.sumOf {
            val commonNeighbours = it.allAround().values.filter { it in garden.position }.size

            when {
                commonNeighbours == 4 -> 0L
                commonNeighbours == 3 -> 1
                commonNeighbours == 2 -> 2
                commonNeighbours == 1 -> 3
                else -> 4
            }

        }
        val area = garden.position.size
        area * perimeter
    }
}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test2")).println("Part one test result") == 140L)
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 1930L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test2")).println("Part one test result") == 80L)
    check(part2(readInputResources(DAY_NAME, "test")).println("Part one test result") == 1206L)
}

private fun part2(input: List<String>): Long {
//_A_
//AAA
    val game = parsePositionGame(input) { it }

    val gardens = game.mapValues { Garden(mutableSetOf(it.key), it.value, game = game) }.toMutableMap()
    for (position in game.keys.sortedWith(compareBy({ it.row }, { it.column }))) {
        val neighbours = position.allAround()
        val gardenValue = game[position]
        var foundGarden: Garden = gardens[position]!!
        neighbours.forEach {
            if (it.value in game.keys && game[it.value] == gardenValue) {
                foundGarden.mergeGarden(gardens[it.value]!!)
                foundGarden.position.forEach {
                    gardens[it] = foundGarden
                }

            }
        }
    }



    return gardens.values.toSet().sumOf { garden ->
        val corners = garden.position.sumOf { cornerCount(it, garden) }


        val area = garden.position.size

        area * corners
    }
}

val possibleCorrners = listOf<(Pair<Position, Garden>) -> Boolean>(
    { (position, garden) -> garden.state(position.north().second) == GardenState.OUT_GARDEN  && garden.state(position.east().second) == GardenState.OUT_GARDEN },
    { (position, garden) -> garden.state(position.south().second) == GardenState.OUT_GARDEN  && garden.state(position.east().second) == GardenState.OUT_GARDEN },

    { (position, garden) -> garden.state(position.north().second) == GardenState.OUT_GARDEN  && garden.state(position.west().second) == GardenState.OUT_GARDEN },
    { (position, garden) -> garden.state(position.south().second) == GardenState.OUT_GARDEN  && garden.state(position.west().second) == GardenState.OUT_GARDEN },


    { (position, garden) -> garden.state(position.north().second) == GardenState.IN_GARDEN  && garden.state(position.east().second) == GardenState.IN_GARDEN  && garden.state(position.northEast()) == GardenState.OUT_GARDEN},
    { (position, garden) -> garden.state(position.south().second) == GardenState.IN_GARDEN  && garden.state(position.east().second) == GardenState.IN_GARDEN && garden.state(position.southEast()) == GardenState.OUT_GARDEN},

    { (position, garden) -> garden.state(position.north().second) == GardenState.IN_GARDEN  && garden.state(position.west().second) == GardenState.IN_GARDEN && garden.state(position.northWest()) == GardenState.OUT_GARDEN},
    { (position, garden) -> garden.state(position.south().second) == GardenState.IN_GARDEN  && garden.state(position.west().second) == GardenState.IN_GARDEN && garden.state(position.southWest()) == GardenState.OUT_GARDEN},


    )


fun cornerCount(position: Position, garden: Garden): Long {

    return possibleCorrners.count { it(position to garden) }.toLong()
}




