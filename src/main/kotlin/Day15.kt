import shared.Utils

class Day15 {

  companion object {
    private fun getInput(): Pair<MutableList<MutableList<Char>>, List<Pair<Int, Int>>>? {
      val input = Utils.readText("/inputs/day15.txt") ?: return null
      val (grid, instructions) = input.split("\r\n\r\n")
      val mappedGrid = grid.split("\r\n").map {
        it.toCharArray().toMutableList()
      }.toMutableList()
      val mappedInstructions =
        // Filter out any white space and map to direction pairs
        instructions.toCharArray().filter { char -> !Character.isWhitespace(char) }.map { instruction ->
          when (instruction) {
            '<' -> Pair(0, -1)
            '^' -> Pair(-1, 0)
            '>' -> Pair(0, 1)
            else -> Pair(1, 0)
          }
        }
      return Pair(mappedGrid, mappedInstructions)
    }

    fun solvePartOne() {
      val (grid, instructions) = getInput() ?: return
      mainLoop@ for (row in grid.indices) {
        for (col in grid[row].indices) {
          if (grid[row][col] != '@') {
            continue
          }
          var currRow = row
          var currCol = col
          for ((y, x) in instructions) {
            val newRow = currRow + y
            val newCol = currCol + x
            val newPos = grid[newRow][newCol]
            when (newPos) {
              'O' -> {
                // Add up all the boxes that should be moved
                var boxCount = 1
                var movable = true
                var nextPos = Pair(newRow + y, newCol + x)
                while (Utils.isPairWithinGridBounds(grid, nextPos)) {
                  when (grid[nextPos.first][nextPos.second]) {
                    // If we hit the edge before we hit an empty space, the boxes are not movable and we should stop
                    '#' -> {
                      movable = false
                      break
                    }

                    'O' -> {
                      boxCount++
                    }
                    // If we hit an empty space, stop looping and move the boxes
                    '.' -> {
                      break
                    }
                  }
                  nextPos = Pair(nextPos.first + y, nextPos.second + x)
                }
                if (movable) {
                  // Work backwards from empty space to add all the boxes
                  for (i in boxCount downTo 0) {
                    grid[nextPos.first - (y * i)][nextPos.second - (x * i)] = 'O'
                  }
                  grid[currRow][currCol] = '.'
                  grid[newRow][newCol] = '@'
                  currRow = newRow
                  currCol = newCol
                }
                // Skip the loop no matter if boxes were movable or not to avoid overlap
                continue
              }

              '#' -> {
                continue
              }

              else -> {
                grid[currRow][currCol] = '.'
                grid[newRow][newCol] = '@'
              }
            }
            currRow = newRow
            currCol = newCol

          }

          // Stop looping as we've already finished all the moves
          break@mainLoop
        }
      }

      var sum = 0
      for (row in grid.indices) {
        for (col in grid[row].indices) {
          if (grid[row][col] == 'O') {
            sum += 100 * row + col
          }
        }
      }

      println("Result: $sum")
    }
  }
}

fun main() {
  Day15.solvePartOne()
}