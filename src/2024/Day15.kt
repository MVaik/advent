package `2024`

import shared.kotlin.GridUtils.Companion.isPairWithinGridBounds
import shared.kotlin.Utils.Companion.readText

class Day15 {

  companion object {
    private fun getInput(): Pair<MutableList<MutableList<Char>>, List<Pair<Int, Int>>>? {
      val input = readText("/inputs/2024/day15.txt") ?: return null
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

    private fun getExpandedInput(): Pair<MutableList<MutableList<Char>>, List<Pair<Int, Int>>>? {
      val input = readText("/inputs/2024/day15.txt") ?: return null
      val (grid, instructions) = input.split("\r\n\r\n")
      // Replace chars before they get turned into arrays, simpler this way
      val mappedGrid =
        grid.replace("#", "##")
          .replace("O", "[]")
          .replace(".", "..")
          .replace("@", "@.")
          .split("\r\n").map {
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
                while (isPairWithinGridBounds(grid, nextPos)) {
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

    private fun canMoveBoxesVertically(grid: MutableList<MutableList<Char>>, pos: Pair<Int, Int>, y: Int): Boolean {
      val nextPos = Pair(pos.first + y, pos.second)
      if (!isPairWithinGridBounds(grid, nextPos)) {
        return false
      }
      val nextChar = grid[nextPos.first][nextPos.second]
      if (nextChar == '#') {
        return false
      }
      if (nextChar == '.') {
        return true
      }
      // Recursively check both chains until we hit either a wall or empty space
      return if (nextChar == ']') {
        canMoveBoxesVertically(grid, Pair(nextPos.first, nextPos.second - 1), y) && canMoveBoxesVertically(
          grid,
          Pair(nextPos.first, nextPos.second),
          y
        )
      } else {
        canMoveBoxesVertically(grid, Pair(nextPos.first, nextPos.second), y) && canMoveBoxesVertically(
          grid,
          Pair(nextPos.first, nextPos.second + 1),
          y
        )
      }
    }

    private fun moveBoxesVertically(grid: MutableList<MutableList<Char>>, pos: Pair<Int, Int>, y: Int) {
      val nextPos = Pair(pos.first + y, pos.second)
      if (!isPairWithinGridBounds(grid, nextPos)) {
        return
      }
      val nextChar = grid[nextPos.first][nextPos.second]
      // Move char one tile
      if (nextChar == '.') {
        grid[nextPos.first][nextPos.second] = grid[pos.first][pos.second]
        grid[pos.first][pos.second] = nextChar
        return
      }
      val alternateCol = if (nextChar == ']') nextPos.second - 1 else nextPos.second + 1
      // Recursively move both branches of chars to make sure all connected boxes are moved too
      moveBoxesVertically(grid, Pair(nextPos.first, alternateCol), y)
      moveBoxesVertically(grid, Pair(nextPos.first, nextPos.second), y)
      grid[nextPos.first][nextPos.second] = grid[pos.first][pos.second]
      grid[pos.first][pos.second] = '.'
    }

    fun solvePartTwo() {
      val (grid, instructions) = getExpandedInput() ?: return
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
            when (val newPos = grid[newRow][newCol]) {
              '[', ']' -> {
                if (y != 0) {
                  val alternateCol = if (newPos == ']') newCol - 1 else newCol + 1
                  // Check if both branches are fine to move, both box sides could essentially be a branching tree
                  val canMoveBoxes =
                    canMoveBoxesVertically(grid, Pair(newRow, newCol), y) && canMoveBoxesVertically(
                      grid,
                      Pair(newRow, alternateCol), y
                    )
                  if (canMoveBoxes) {
                    // Move both branches
                    moveBoxesVertically(grid, Pair(newRow, alternateCol), y)
                    moveBoxesVertically(grid, Pair(newRow, newCol), y)
                    // Move robot and update the current position
                    grid[currRow][currCol] = '.'
                    grid[newRow][newCol] = '@'
                    currRow = newRow
                    currCol = newCol
                  }
                } else {
                  // Use the same logic as part 1 for horizontal moves, just supports both brackets for box
                  // Add up all the boxes that should be moved
                  var boxPieces = 1
                  var movable = true
                  var nextPos = Pair(newRow, newCol + x)
                  while (isPairWithinGridBounds(grid, nextPos)) {
                    when (grid[nextPos.first][nextPos.second]) {
                      // If we hit the edge before we hit an empty space, the boxes are not movable and we should stop
                      '#' -> {
                        movable = false
                        break
                      }

                      '[', ']' -> {
                        boxPieces++
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
                    var currentPiece = if (x > 0) ']' else '['
                    for (i in boxPieces downTo 0) {
                      grid[nextPos.first][nextPos.second - (x * i)] = currentPiece
                      // Alternate between pieces to create proper boxes
                      currentPiece = if (currentPiece == ']') '[' else ']'
                    }
                    // Move robot into the new place and update our current position
                    grid[currRow][currCol] = '.'
                    grid[newRow][newCol] = '@'
                    currRow = newRow
                    currCol = newCol
                  }
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
          break@mainLoop
        }
      }

      var sum = 0
      for (row in grid.indices) {
        for (col in grid[row].indices) {
          if (grid[row][col] == '[') {
            sum += 100 * row + col
          }
        }
      }

      println("Result: $sum")
    }
  }
}

fun main() {
  Day15.solvePartTwo()
}