package day19

import State
import println
import readInputResources

private const val DAY_NAME = "Day19"


fun main() {
//    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
//    part1(input).println("Part one result:")
    part2(input).println("Part two result:").let {
        check(it > 630998144902517L)
        check(it == 678536865274732)
    }
}


private fun part1(input: List<String>): Long {
    val towels = input[0].split(", ")
    val patterns = input.drop(2)

    return patterns.filter {
        val result = isPossible(it, towels).isNotEmpty()
        result.println("Pattern $it is possible:")
        result
    }.size.toLong()
}

data class Combination(
    val pattern: String,
    val towels: List<String>,
    val leftToProcess: String = pattern,
    val parent: Combination? = null
) {
    val childs: MutableList<Combination> = mutableListOf()
    val completed = false

    fun addTowel(towel: String): Combination {
        val child = Combination(
            pattern = pattern,
            towels = towels + towel,
            leftToProcess = leftToProcess.substring(towel.length),
            parent = this
        )
        childs.add(child)
        return child
    }


}

private fun isPossible(patter: String, towels: List<String>): List<Combination> {
    val state = State<Combination, Combination>(mutableListOf(Combination(patter, mutableListOf())), mutableListOf())

    while (state.itemsToProcess.isNotEmpty()) {

        val item = state.itemsToProcess.removeFirst()

        if (towels.contains(item.leftToProcess)) {
            item.println("Added")
            state.acceptResult(item)
            break

        } else {
            towels
                .filter {
                    item.leftToProcess.startsWith(it)
                }.forEach {
                    state.addToProcessing(item.addTowel(it))
                }

        }
        state.itemsToProcess.sortBy { it.leftToProcess.length }
    }

    return state.result

}

private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 6L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == 16L)
}

private fun part2(input: List<String>): Long {
    val towels = input[0].split(", ")
    val patterns = input.drop(2)
    val cache: MutableMap<String, Long> = mutableMapOf()
    return patterns.sumOf {
        val result = getCombinations(it, towels, cache)
        result.println("Pattern $it is possible:")
        result
    }
}

private fun getCombinations(pattern: String, towels: List<String>, cache: MutableMap<String, Long>): Long {
    return cache.getOrPut(pattern) {
        val result = if (towels.contains(pattern)) 1 else 0
        result + towels
            .filter {
                pattern.startsWith(it) && pattern != it
            }.sumOf {
                getCombinations(pattern.substring(it.length), towels, cache)
            }

    }
}

