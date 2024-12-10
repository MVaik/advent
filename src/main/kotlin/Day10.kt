import shared.Utils

class Day10 {
  companion object {

    private fun addUniqueTrails(
      grid: List<String>,
      position: Pair<Int, Int>,
      expectedNum: Int,
      foundTrailEnds: MutableSet<Pair<Int, Int>>
    ) {
      val currentPosValue = grid[position.first][position.second].digitToInt()
      if (currentPosValue != expectedNum) {
        return
      }
      // If we're on the last number, we found it
      if (currentPosValue == 9) {
        foundTrailEnds.add(position)
        return
      }
      for (direction in Utils.cardinalDirections) {
        val newPos = Pair(position.first + direction.first, position.second + direction.second)
        if (Utils.isPairWithinGridBounds(grid, newPos)) {
          addUniqueTrails(grid, newPos, expectedNum + 1, foundTrailEnds)
        }
      }
    }

    private fun countValidTrails(
      grid: List<String>,
      position: Pair<Int, Int>,
      expectedNum: Int,
    ): Int {
      val currentPosValue = grid[position.first][position.second].digitToInt()
      if (currentPosValue != expectedNum) {
        return 0
      }
      // If we're on the last number, we found it
      if (currentPosValue == 9) {
        return 1
      }
      var foundTrails = 0
      for (direction in Utils.cardinalDirections) {
        val newPos = Pair(position.first + direction.first, position.second + direction.second)
        if (Utils.isPairWithinGridBounds(grid, newPos)) {
          foundTrails += countValidTrails(grid, newPos, expectedNum + 1)
        }
      }
      return foundTrails
    }

    fun sumUniqueTrails() {
      val input = Utils.readLines("/inputs/day10.txt") ?: return
      var sum = 0
      for (row in input.indices) {
        for (col in input[row].indices) {
          if (input[row][col] == '0') {
            val foundTrails = mutableSetOf<Pair<Int, Int>>()
            for (direction in Utils.cardinalDirections) {
              val newPoint = Pair(row + direction.first, col + direction.second)
              if (Utils.isPairWithinGridBounds(input, newPoint)) {
                addUniqueTrails(input, newPoint, 1, foundTrails)
              }
            }
            sum += foundTrails.size
          }
        }
      }

      println("Result: %s".format(sum))
    }

    fun sumAllTrails() {
      val input = Utils.readLines("/inputs/day10.txt") ?: return
      var sum = 0
      for (row in input.indices) {
        for (col in input[row].indices) {
          if (input[row][col] == '0') {
            for (direction in Utils.cardinalDirections) {
              val newPoint = Pair(row + direction.first, col + direction.second)
              if (Utils.isPairWithinGridBounds(input, newPoint)) {
                sum += countValidTrails(input, newPoint, 1)
              }
            }
          }
        }
      }

      println("Result: %s".format(sum))
    }
  }
}

fun main(args: Array<String>) {
  Day10.sumAllTrails()
}