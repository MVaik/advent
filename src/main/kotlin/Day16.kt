import shared.GridUtils.Companion.cardinalDirections
import shared.GridUtils.Companion.isPairWithinGridBounds
import shared.GridUtils.Companion.walkGrid
import shared.Utils
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


    fun solvePartOne() {
      val input = Utils.readLines("/inputs/day16_example4.txt") ?: return
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
          cardinalDirections.sortedBy { getDirectionDiffCost(currDirection, it) }.forEach { direction ->
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
  }
}

fun main() {
  Day16.solvePartOne()
}