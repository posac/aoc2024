package day23

import println
import readInputResources

private const val DAY_NAME = "Day23"


fun main() {
//    checkPart1()
//    checkPart2()


    val input = readInputResources(DAY_NAME, "input")
//    part1(input).println("Part one result:")
    part2(input).println("Part two result:")
}


private fun part1(input: List<String>): Long {
    val mapping = parseInput(input)


    val groups = findGroups(mapping, 3)
    groups.println()
    return groups.filter { it.any { it.startsWith("t") } }.size.toLong()
}

private fun parseInput(input: List<String>): MutableMap<String, MutableSet<String>> {
    val mapping = mutableMapOf<String, MutableSet<String>>()

    input.forEach {
        it.println("Processing")
        val (from, to) = it.split("-")


        mapping.getOrPut(from) { mutableSetOf() }.add(to)
        mapping.getOrPut(to) { mutableSetOf() }.add(from)
    }
    return mapping
}

fun findGroups(mapping: MutableMap<String, MutableSet<String>>, computers: Int): Set<Set<String>> {
    return mapping.entries.map { (key, value) ->
        value.flatMap { second ->
            val connected = mapping[second]!!
            connected.intersect(value).map {
                setOf(key, second, it)
            }
        }
    }.flatten().toSet()

}


private fun checkPart1() {
    check(part1(readInputResources(DAY_NAME, "test")).println("Part one test result") == 7L)
}

private fun checkPart2() {
    check(part2(readInputResources(DAY_NAME, "test")).println("Part two test result") == "co,de,ka,ta")
}

private fun part2(input: List<String>): String {
    val connections = parseInput(input)

//    val groups = findBiggest(connections)
    val graph = Graph()
    connections.forEach { t, u ->
        u.forEach { it ->
            graph.addEdge(t, it)
        }

    }

    val graphs = graph.findCliques()
    return ""
//    return groups.maxBy { it.size }.sortedBy { it }.joinToString(",")

}


private fun findBiggest(connections: MutableMap<String, MutableSet<String>>): Set<MutableSet<String>> {

    var groups = connections.entries.flatMap { (key, value) ->
        value.map { mutableSetOf(key, it) }
    }.toSet().toMutableList()
//    var changed = true
//    while (changed) {
//        val newGroups = mutableListOf<MutableSet<String>>()
//        changed = false
//        groups.combinationSequence().filter { (first, second) ->
//            (first.intersect(second)).size != first.size
//                    && second.intersect(first).size != second.size
//                    && groupsAreInterConnected(first, second, connections)
//
//        }.forEach {
//            val (first, second) = it
//
//            newGroups.add(first.union(second).toMutableSet())
//
//            changed = true
//        }
//        if (newGroups.isNotEmpty())
//            groups = newGroups
//
//        groups.size.println("Groups size")
//
//    }


    return groups.toSet()
}

private fun groupsAreInterConnected(
    first: MutableSet<String>,
    second: MutableSet<String>,
    connections: MutableMap<String, MutableSet<String>>
): Boolean {
    val result = first.all { firstCP ->
        second.all { secondPC ->
            connections[firstCP]!!.contains(secondPC)
        }
    }
    return result
}


// Define a Graph Class
class Graph {
    private val adjacencyList: MutableMap<String, MutableList<String>> = mutableMapOf()
    private val vertex: MutableSet<String> = mutableSetOf()

    fun addEdge(source: String, target: String) {
        vertex.add(source)
        vertex.add(target)
        adjacencyList.getOrPut(source) { mutableListOf() }.add(target)
        adjacencyList.getOrPut(target) { mutableListOf() }.add(source)
    }


    private fun dfs(node: String, visited: MutableSet<String>, component: MutableSet<String>) {
        visited.add(node)
        component.add(node)

        adjacencyList[node]?.forEach { neighbor ->
            if (neighbor !in visited) {
                dfs(neighbor, visited, component)
            }
        }
    }

    fun getConnectedComponents(): List<Set<String>> {
        val visited = mutableSetOf<String>()
        val components = mutableListOf<Set<String>>()

        for (node in vertex) {
            if (node !in visited) {
                val component = mutableSetOf<String>()
                dfs(node, visited, component)
                components.add(component)
            }
        }

        return components
    }

    fun splitGraph(): List<Graph> {
        val components = getConnectedComponents()
        val subgraphs = mutableListOf<Graph>()

        for (component in components) {
            val subgraph = Graph()

            component.forEach { node ->
                adjacencyList[node]?.forEach { neighbor ->
                    if (neighbor in component) {
                        subgraph.addEdge(node, neighbor)
                    }
                }
            }

            subgraphs.add(subgraph)
        }

        return subgraphs
    }


    fun findCliques(): List<Set<String>> {
        val cliques = mutableListOf<Set<String>>()
        val candidates = adjacencyList.keys.toMutableSet()
        val potentialClique = adjacencyList[candidates.first()]!!.toMutableSet()
        val alreadyFound = mutableSetOf<String>()


        findCliquesRecursive(candidates, potentialClique, alreadyFound, cliques)
        return cliques
    }

    // Recursive function to find cliques
    private fun findCliquesRecursive(
        candidates: Set<String>,
        potentialClique: MutableSet<String>,
        alreadyFound: MutableSet<String>,
        cliques: MutableList<Set<String>>
    ) {
        if (candidates.isEmpty() && alreadyFound.isEmpty()) {
            cliques.add(potentialClique.toSet())
            return
        }

        val candidatesList = candidates.toList()
        for (node in candidatesList) {
            val newCandidates = candidates.intersect(adjacencyList[node]!!).toMutableSet()
            val newAlreadyFound = alreadyFound.intersect(adjacencyList[node]!!).toMutableSet()

            potentialClique.add(node)
            findCliquesRecursive(newCandidates, potentialClique, newAlreadyFound, cliques)
            potentialClique.remove(node)

            alreadyFound.add(node)
        }
    }
}

