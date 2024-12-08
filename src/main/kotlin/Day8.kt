import shared.Utils

class Day8 {
  companion object {
    fun countAntiNodes() {
      val input = Utils.readLines("/inputs/day8.txt") ?: return

      val visitedGrid = HashMap<Char, MutableSet<Pair<Int, Int>>>()
      for (row in input.indices) {
        for (col in input[row].indices) {
          if (input[row][col] == '.') {
            continue
          }
          val char = input[row][col]
          // Keep track of all the positions for each character
          if (visitedGrid.containsKey(char)) {
            visitedGrid[char]?.add(Pair(row, col))
          } else {
            visitedGrid[char] = mutableSetOf(Pair(row, col))
          }
        }
      }

      val uniquePositions = mutableSetOf<Pair<Int, Int>>()
      visitedGrid.forEach { gridPos ->
        gridPos.value.forEach { pair ->
          gridPos.value.forEach { secondPair ->
            if (pair != secondPair) {
              val diffedPair = Pair(secondPair.first - pair.first, secondPair.second - pair.second)
              // Generate new pairs in both directions via their difference
              val newPair = Pair(pair.first - diffedPair.first, pair.second - diffedPair.second)
              val newSecondPair = Pair(secondPair.first + diffedPair.first, secondPair.second + diffedPair.second)
              arrayOf(newPair, newSecondPair).forEach { generatedPair ->
                if (generatedPair.first >= 0 && generatedPair.first < input.size && generatedPair.second >= 0 && generatedPair.second < input[0].length) {
                  uniquePositions.add(generatedPair)
                }
              }
            }
          }
        }
      }
      val count = uniquePositions.size
      println("Result: %s".format(count))
    }

    fun countAnyPositionAntiNodes() {
      val input = Utils.readLines("/inputs/day8.txt") ?: return

      val visitedGrid = HashMap<Char, MutableSet<Pair<Int, Int>>>()
      for (row in input.indices) {
        for (col in input[row].indices) {
          if (input[row][col] == '.') {
            continue
          }
          val char = input[row][col]
          if (visitedGrid.containsKey(char)) {
            visitedGrid[char]?.add(Pair(row, col))
          } else {
            visitedGrid[char] = mutableSetOf(Pair(row, col))
          }
        }
      }
      val uniquePositions = mutableSetOf<Pair<Int, Int>>()
      visitedGrid.forEach { gridPos ->
        gridPos.value.forEach { pair ->
          gridPos.value.forEach { secondPair ->
            if (pair != secondPair) {
              val diffedPair = Pair(secondPair.first - pair.first, secondPair.second - pair.second)
              var newPair = Pair(pair.first - diffedPair.first, pair.second - diffedPair.second)
              // Generate new pairs in each direction until out of bounds
              while (newPair.first >= 0 && newPair.first < input.size && newPair.second >= 0 && newPair.second < input[0].length) {
                // Ignore any letter positions this time, as we'll be adding them separately
                if (input[newPair.first][newPair.second] == '.') {
                  uniquePositions.add(newPair)
                }
                newPair = Pair(newPair.first - diffedPair.first, newPair.second - diffedPair.second)
              }

              var newSecondPair = Pair(secondPair.first + diffedPair.first, secondPair.second + diffedPair.second)
              while (newSecondPair.first >= 0 && newSecondPair.first < input.size && newSecondPair.second >= 0 && newSecondPair.second < input[0].length) {
                if (input[newSecondPair.first][newSecondPair.second] == '.') {

                  uniquePositions.add(newSecondPair)
                }
                newSecondPair = Pair(newSecondPair.first + diffedPair.first, newSecondPair.second + diffedPair.second)
              }
            }
          }
        }
      }
      // Add new unique positions and how many unique letter positions we had
      val uniquePositionsCount = uniquePositions.size
      var letterPositionsCount = 0
      visitedGrid.map { entry -> entry.value }.toSet().forEach { letterPositionsCount += it.size }
      println("Result: %s".format(uniquePositionsCount + letterPositionsCount))
    }
  }
}

fun main(args: Array<String>) {
  Day8.countAnyPositionAntiNodes()
}
