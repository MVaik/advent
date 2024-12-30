package `2024`

import shared.kotlin.Utils

class Day19 {
  companion object {
    private fun getInput(): Pair<Set<String>, Set<String>>? {
      val input = Utils.readText("/inputs/2024/day19.txt") ?: return null
      val (towelsRow, designs) = input.split("\r\n\r\n")
      val towels = towelsRow.split(", ").toSet()
      val designsArr = designs.split("\r\n").toSet()
      return Pair(towels, designsArr)
    }

    fun solvePartOne() {
      val (towels, designs) = getInput() ?: return
      val towelsRegex = Regex("^(?:${towels.joinToString("|")})+$")
      var possibleDesigns = 0
      for (design in designs) {
        if (towelsRegex.containsMatchIn(design)) {
          possibleDesigns++
        }
      }
      println(possibleDesigns)
    }

    private fun countTowelPossibilities(
      input: String,
      towels: Set<String>,
      knownPossibilities: HashMap<String, Long>
    ): Long {
      if (knownPossibilities.contains(input)) {
        return knownPossibilities[input]!!
      }
      var possibilities = 0L
      if (towels.contains(input)) {
        possibilities++
      }
      for (i in 1..<input.length) {
        if (towels.contains(input.slice(0..<i))) {
          val count = countTowelPossibilities(input.slice(i..<input.length), towels, knownPossibilities)
          possibilities += count
        }
      }
      knownPossibilities[input] = possibilities
      return possibilities
    }

    fun solvePartTwo() {
      val (towels, designs) = getInput() ?: return
      var possibleDesigns = 0L
      val knownPossibilities = HashMap<String, Long>()
      for (design in designs) {
        possibleDesigns += countTowelPossibilities(design, towels, knownPossibilities)
      }
      println(possibleDesigns)
    }
  }
}

fun main() {
  Day19.solvePartTwo()
}