import shared.*
import shared.GridUtils.Companion.cardinalDirectionPairs
import shared.GridUtils.Companion.cardinalDirections
import shared.GridUtils.Companion.isCellWithinGridBounds
import shared.GridUtils.Companion.isPairWithinGridBounds
import shared.GridUtils.Companion.walkGrid
import kotlin.math.abs

class Day16 {
  companion object {

    private fun getDirectionDiff(first: Pair<Int, Int>, second: Pair<Int, Int>): Pair<Int, Int> {
      return Pair(abs(first.first - second.first), abs(first.second - second.second))
    }

    private fun getDirectionDiffCost(first: Pair<Int, Int>, second: Pair<Int, Int>): Int {
      val directionDiff = getDirectionDiff(first, second)
      // If both y and x change, we've moved clockwise or counter-clockwise
      if (directionDiff.first == 1 && directionDiff.second == 1) {
        return 1000
        // If either one of them is 2, it means we went the complete opposite direction, which is 2 turns
      } else if (directionDiff.first == 2 || directionDiff.second == 2) {
        return 2000
      }
      return 0
    }

    private fun getDirectionDiff(first: Direction, second: Direction): Direction {
      return Direction(abs(first.y - second.y), abs(first.x - second.x))
    }

    private fun getDirectionDiffCost(first: Direction, second: Direction): Int {
      val directionDiff = getDirectionDiff(first, second)
      // If both y and x change, we've moved clockwise or counter-clockwise
      if (directionDiff.y == 1 && directionDiff.x == 1) {
        return 1000
        // If either one of them is 2, it means we went the complete opposite direction, which is 2 turns
      } else if (directionDiff.y == 2 || directionDiff.x == 2) {
        return 2000
      }
      return 0
    }


    fun solvePartOne() {
      val input = Utils.readLines("/inputs/day16.txt") ?: return
      var sum = Int.MAX_VALUE
      val baseDirection = Pair(0, 1)
      input.walkGrid { char, row, col, grid ->
        if (char != 'S') {
          return@walkGrid
        }
        val queue = ArrayDeque<List<Pair<Int, Int>>>()
        queue.add(
          listOf(
            Pair(row, col), baseDirection, Pair(0, -1)
          )
        )
        val visited = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Int>()
        while (queue.size > 0) {
          val (currPosition, currDirection, currSum) = queue.removeFirst()
          if (grid[currPosition.first][currPosition.second] == 'E') {
            if (currSum.first < sum) {
              sum = currSum.first
            }
            continue
          }
          cardinalDirectionPairs.sortedBy { getDirectionDiffCost(currDirection, it) }.forEach { direction ->
            val diffSum = getDirectionDiffCost(currDirection, direction)
            val nextPos = Pair(currPosition.first + direction.first, currPosition.second + direction.second)
            val newSum = currSum.first + diffSum + 1
            val positionDir = Pair(nextPos, direction)
            if (isPairWithinGridBounds(
                grid,
                nextPos
              ) && grid[nextPos.first][nextPos.second] != '#' && (!visited.contains(
                positionDir
              ) || visited[positionDir]!! >= newSum)
            ) {
              visited[positionDir] = newSum
              queue.add(
                listOf(
                  nextPos, direction, Pair(newSum, -1)
                )
              )
            }
          }
        }
      }
      println("Result: $sum")
    }

    fun solvePartTwo() {
      val input = Utils.readLines("/inputs/day16.txt") ?: return
      var sum = Int.MAX_VALUE
      val baseDirection = Direction(0, 1)
      var paths = mutableSetOf<Position>()
      input.walkGrid { char, row, col, grid ->
        if (char != 'S') {
          return@walkGrid
        }
        val queue = ArrayDeque<GridCell>()
        queue.add(
          GridCell(Position(row, col), baseDirection, value = 0, path = mutableListOf())
        )
        val visited = mutableMapOf<DirectedPosition, Int>()
        while (queue.size > 0) {
          val curr = queue.removeFirst()
          if (grid[curr.pos.row][curr.pos.col] == 'E') {
            if (curr.value < sum) {
              // If we found a new low, clear the previous positions
              paths = mutableSetOf()
            }
            if (curr.value <= sum) {
              sum = curr.value
              paths.add(curr.pos)
              // Save all unique positions for the lowest sum
              curr.path?.let {
                it.forEach { cell ->
                  paths.add(cell.pos)
                }
              }
            }
          }
          cardinalDirections.sortedBy { getDirectionDiffCost(curr.direction, it) }.forEach { direction ->
            val diffSum = getDirectionDiffCost(curr.direction, direction)
            val nextPos = Position(curr.pos.row + direction.y, curr.pos.col + direction.x)
            val newSum = curr.value + diffSum + 1
            val positionDir = DirectedPosition(nextPos, direction)
            // Keep track of previous path
            val newCell = GridCell(nextPos, direction, value = newSum, path = curr.addToPath(curr))
            // Make sure new cell is in grid bounds, is not hitting an edge
            // and either is not already visited or is cheaper than the visited one
            if (isCellWithinGridBounds(
                grid,
                newCell
              ) && grid[nextPos.row][nextPos.col] != '#' && (!visited.contains(
                positionDir
              ) || visited[positionDir]!! >= newSum)
            ) {
              visited[positionDir] = newSum
              queue.add(
                newCell
              )
            }
          }
        }
      }
      println("Result: $sum, size: ${paths.size}")
    }
  }
}

fun main() {
  Day16.solvePartTwo()
}