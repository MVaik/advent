import shared.Utils

class Day19 {
  companion object {
    private fun getInput(): Pair<List<String>, Set<String>>? {
      val input = Utils.readText("/inputs/day19.txt") ?: return null
      val (towelsRow, designs) = input.split("\r\n\r\n")
      val towels = towelsRow.split(", ")
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
  }
}

fun main() {
}