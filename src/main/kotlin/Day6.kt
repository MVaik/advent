import shared.Utils

class Day6 {
  companion object {
    private val nextDirectionMap = hashMapOf(
      Pair(-1, 0) to Pair(0, 1),
      Pair(0, 1) to Pair(1, 0),
      Pair(1, 0) to Pair(0, -1),
      Pair(0, -1) to Pair(-1, 0)
    )

    fun countVisitedCells() {
      val input = Utils.readLines("/inputs/day6.txt")
      val visitedGrid = HashMap<Int, MutableSet<Int>>()
      if (input == null) {
        return
      }
      for (row in input.indices) {
        for (col in input[row].indices) {
          if (input[row][col] == '^') {
            // Start traveling the path if we found the guard cell
            var direction = Pair(-1, 0)
            if (visitedGrid.containsKey(row)) {
              visitedGrid[row]?.add(col)
            } else {
              visitedGrid[row] = mutableSetOf(col)
            }
            var nextRow = row + direction.first
            var nextCol = col + direction.second
            // Keep traveling until we go out of bounds
            while (nextRow >= 0 && nextRow < input.size && nextCol >= 0 && nextCol < input[row].length) {
              if (input[nextRow][nextCol] == '#') {
                nextRow -= direction.first
                nextCol -= direction.second
                direction = nextDirectionMap[direction]!!
              }
              // Keep track of unique visited cells
              if (visitedGrid.containsKey(nextRow)) {
                visitedGrid[nextRow]?.add(nextCol)
              } else {
                visitedGrid[nextRow] = mutableSetOf(nextCol)
              }

              nextRow += direction.first
              nextCol += direction.second
            }
          }
        }
      }

      // Count grid entries
      val count = visitedGrid.entries.map {
        it.value.size
      }.reduce { acc, int -> acc + int }
      println("Result: %s".format(count))
    }

    fun findLoopingCells() {
      val input = Utils.readLines("/inputs/day6.txt")
      val visitedGrid = HashMap<Int, MutableSet<Int>>()
      var count = 0
      if (input == null) {
        return
      }
      // Brute force each cell
      for (blockedRow in input.indices) {
        for (blockedCol in input[blockedRow].indices) {
          for (row in input.indices) {
            for (col in input[row].indices) {
              if (input[row][col] == '^') {
                var direction = Pair(-1, 0)
                if (visitedGrid.containsKey(row)) {
                  visitedGrid[row]?.add(col)
                } else {
                  visitedGrid[row] = mutableSetOf(col)
                }
                var nextRow = row + direction.first
                var nextCol = col + direction.second
                var iterationCount = 0
                while (nextRow >= 0 && nextRow < input.size && nextCol >= 0 && nextCol < input[row].length) {
                  // If we've travel more than the grid size, we're in a loop.
                  if (iterationCount > input.size * input[row].length) {
                    count++
                    break
                  }
                  if (input[nextRow][nextCol] == '#' || (nextRow == blockedRow && nextCol == blockedCol)) {
                    nextRow -= direction.first
                    nextCol -= direction.second
                    direction = nextDirectionMap[direction]!!
                  }
                  if (visitedGrid.containsKey(nextRow)) {
                    visitedGrid[nextRow]?.add(nextCol)
                  } else {
                    visitedGrid[nextRow] = mutableSetOf(nextCol)
                  }

                  nextRow += direction.first
                  nextCol += direction.second
                  iterationCount++
                }
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
  Day6.findLoopingCells()
}
