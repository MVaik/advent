import shared.Utils

class Day13 {
  companion object {
    fun findCheapestPrizeAcquisition(solveFirst: Boolean = false) {
      val input = Utils.readText("/inputs/day13.txt")?.split("\n\n") ?: return
      // Get x, y and result values via regex
      val inputRegex = Regex("(?<x>(?<=X\\+)\\d+)|(?<y>(?<=Y\\+)\\d+)|(?<result>\\d+)", RegexOption.MULTILINE)
      val problems = input.map {
        val data = hashMapOf<String, MutableList<Long>>()
        val matches = inputRegex.findAll(it)
        for (match in matches) {
          for (value in listOf("x", "y", "result")) {
            if (match.groups[value] != null) {
              val parsedVal = match.groups[value]!!.value.toLong()
              if (data[value] != null) {
                data[value]?.add(parsedVal)
              } else {
                data[value] = mutableListOf(parsedVal)
              }
            }
          }
        }
        data
      }
      var tokensSum = 0L
      for (problem in problems) {
        val (firstXValue, secondXValue) = problem["x"]!!
        val (firstYValue, secondYValue) = problem["y"]!!
        var (firstResult, secondResult) = problem["result"]!!
        if (!solveFirst) {
          firstResult += 10000000000000
          secondResult += 10000000000000
        }
        // Linear systems or something idk
        val firstPart = (firstResult * secondYValue - secondResult * secondXValue)
        val secondPart = (firstXValue * secondYValue - firstYValue * secondXValue)
        // If there's anything remaining after division, the problem is not solvable
        if (firstPart % secondPart != 0L) {
          continue
        }
        val firstCount = firstPart / secondPart
        // Use first count to find the second count
        val secondCount = (firstResult - firstXValue * firstCount) / secondXValue
        tokensSum += firstCount * 3 + secondCount
      }
      println("Result: %s".format(tokensSum))
    }
  }
}

fun main() {
  Day13.findCheapestPrizeAcquisition()
}