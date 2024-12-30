package `2024`

import shared.Utils


class Day23 {
  companion object {


    // Bron-Kerbosch maximal clique algo
    private fun bronKerbosch(
      currentClique: Set<String>,
      potentialComputers: MutableSet<String>,
      processedComputers: MutableSet<String>,
      graph: HashMap<String, MutableSet<String>>
    ): Set<Set<String>> {
      val cliques = mutableSetOf<Set<String>>()
      if (potentialComputers.isEmpty() && processedComputers.isEmpty()) {
        cliques.add(HashSet(currentClique))
      }
      while (potentialComputers.isNotEmpty()) {
        val computer = potentialComputers.iterator().next()
        val newClique = HashSet(currentClique)
        newClique.add(computer)
        val newPotentialComputers = HashSet(potentialComputers)
        newPotentialComputers.retainAll(graph[computer]!!)
        val newlyProcessedComputers = HashSet(processedComputers)
        newlyProcessedComputers.retainAll(graph[computer]!!)
        cliques.addAll(
          bronKerbosch(newClique, newPotentialComputers, newlyProcessedComputers, graph)
        )
        potentialComputers.remove(computer)
        processedComputers.add(computer)
      }
      return cliques
    }

    fun solvePartOne() {
      val input = Utils.readLines("/inputs/2024/day23.txt") ?: return
      val connections = HashMap<String, MutableSet<String>>()
      for (connection in input) {
        val (firstComputer, secondComputer) = connection.split("-")
        connections.getOrPut(firstComputer) { mutableSetOf() }.add(secondComputer)
        connections.getOrPut(secondComputer) { mutableSetOf() }.add(firstComputer)
      }
      val tripleConnected = mutableSetOf<Set<String>>()
      for (connection in connections) {
        for (i in connection.value) {
          for (j in connection.value) {
            if (i == j) {
              continue
            }
            if (connections[i]!!.contains(j)) {
              tripleConnected.add(setOf(connection.key, i, j))
            }
          }
        }
      }


      val tripleConnectionsStartingWithT = tripleConnected.mapNotNull { connection ->
        if (connection.any { computer -> computer.startsWith("t") }) connection else null
      }.count()

      println("Result: $tripleConnectionsStartingWithT")
    }

    fun solvePartTwo() {
      val input = Utils.readLines("/inputs/2024/day23.txt") ?: return
      val connections = HashMap<String, MutableSet<String>>()
      for (connection in input) {
        val (firstComputer, secondComputer) = connection.split("-")
        connections.getOrPut(firstComputer) { mutableSetOf() }.add(secondComputer)
        connections.getOrPut(secondComputer) { mutableSetOf() }.add(firstComputer)
      }

      val allCliques = bronKerbosch(
        HashSet(), connections.keys,
        HashSet(), connections
      ).maxBy { it.size }.sorted().joinToString(",")

      println("Result: $allCliques")
    }
  }
}

fun main() {
  Day23.solvePartTwo()
}