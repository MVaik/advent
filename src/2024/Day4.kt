package `2024`

import shared.kotlin.Utils

class Day4 {
  companion object {
    private val expectedNextLetter = hashMapOf('X' to 'M', 'M' to 'A', 'A' to 'S', 'S' to '-')
    private val allDirections = listOf(
      Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
      Pair(0, -1), Pair(0, 1),
      Pair(1, -1), Pair(1, 0), Pair(1, 1)
    )
    private val halfOfCardinalDirections = listOf(
      Pair(-1, -1), Pair(-1, 1),
    )
    private val cardinalDirectionOpposites =
      mapOf(Pair(-1, -1) to Pair(1, 1), Pair(-1, 1) to Pair(1, -1))

    fun findXmasOccurrences() {
      val input = Utils.readLines("/inputs/2024/day4.txt")
      var count = 0
      if (input != null) {
        for (row in input.indices) {
          for (col in input[row].indices) {
            if (input[row][col] == 'X') {
              // Check for the next letter in all directions
              for ((y, x) in allDirections) {
                var nextChar = expectedNextLetter['X']
                var newRow = row + y
                var newCol = col + x
                while (nextChar != null) {
                  // Keep going in the same direction if the letters are correct
                  if (newRow >= 0 && newRow < input.size && newCol >= 0 && newCol < input[newRow].length && input[newRow][newCol] == nextChar) {
                    nextChar = expectedNextLetter[nextChar]
                    newRow += y
                    newCol += x
                    // Increment count if we've traveled through the chain to find an S
                    if (nextChar == '-') {
                      count++
                      break
                    }
                  } else {
                    break
                  }
                }
              }
            }
          }
        }
      }
      println("Result: %s".format(count))
    }

    fun findXShapeOccurrences() {
      val input = Utils.readLines("/inputs/2024/day4.txt")
      var count = 0
      if (input != null) {
        for (row in input.indices) {
          for (col in input[row].indices) {
            if (input[row][col] == 'A') {
              var masCount = 0
              // Check for the next letter in all directions
              for (direction in halfOfCardinalDirections) {
                val (y, x) = direction
                val newRow = row + y
                val newCol = col + x
                // Make sure the new coordinate is in bounds
                if (newRow >= 0 && newRow < input.size && newCol >= 0 && newCol < input[newRow].length) {
                  val char = input[newRow][newCol]
                  if (char != 'M' && char != 'S') {
                    // No point continuing if a cardinal direction is not one of these characters
                    break
                  }
                  val (oppY, oppX) = cardinalDirectionOpposites[direction]!!
                  val expectedOpp = if (char == 'M') 'S' else 'M'
                  val oppRow = row + oppY
                  val oppCol = col + oppX
                  // Make sure opposite coords are in bounds and match the expected opposite character
                  if (oppRow >= 0 && oppRow < input.size && oppCol >= 0 && oppCol < input[oppRow].length && input[oppRow][oppCol] == expectedOpp) {
                    masCount++
                  }
                }
              }
              if (masCount == 2) {
                count++
              }
            }
          }
        }
      }
      println("Result: %s".format(count))
    }
  }
}

fun main(args: Array<String>) {
  Day4.findXShapeOccurrences()
}
