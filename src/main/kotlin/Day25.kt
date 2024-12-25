import shared.GridUtils.Companion.walkGrid
import shared.Utils

class Day25 {
  companion object {
    private const val HEIGHT = 5
    fun solvePartOne() {
      val input = Utils.readText("/inputs/day25.txt") ?: return

      val locksAndKeys = input.split("\r\n\r\n").map { it.split("\r\n") }
      val locks = mutableSetOf<List<Int>>()
      val keys = mutableSetOf<List<Int>>()

      for (item in locksAndKeys) {
        var topRowFilledColumns = 0
        var bottomRowFilledColumns = 0
        val columns = MutableList(item[0].length) { -1 }
        item.walkGrid { element: Char, rowIndex: Int, colIndex: Int ->
          if (element == '#') {
            if (rowIndex == 0) {
              topRowFilledColumns++
            } else if (rowIndex == HEIGHT) {
              bottomRowFilledColumns++
            }
            columns[colIndex]++
          }
        }
        if (topRowFilledColumns > bottomRowFilledColumns) {
          locks.add(columns)
        } else {
          keys.add(columns)
        }
      }

      val combinations = mutableSetOf<Pair<List<Int>, List<Int>>>()
      for (lock in locks) {
        val remainingSpace = lock.map { HEIGHT - it }
        val matchingKeys = keys.filter { key ->
          var matches = true
          for (column in key.indices) {
            if (key[column] > remainingSpace[column]) {
              matches = false
              break
            }
          }
          matches
        }
        for (matchingKey in matchingKeys) {
          combinations.add(Pair(lock, matchingKey))
        }
      }
      println("Result: ${combinations.size}")
    }
  }
}

fun main() {
  Day25.solvePartOne()
}