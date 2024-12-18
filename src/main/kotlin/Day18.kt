import shared.*
import shared.Utils.Companion.NUMBER_REGEX

class Day18 {
  companion object {
    private const val GRID_SIZE = 70
    private const val BYTES_COUNT = 1024
    fun solvePartOne() {
      val input = Utils.readText("/inputs/day18.txt") ?: return
      val nums = NUMBER_REGEX.findAll(input).toList()
      val blockedPaths = mutableSetOf<Position>()

      for (i in 0..<BYTES_COUNT * 2 step 2) {
        blockedPaths.add(Position(nums[i + 1].value.toInt(), nums[i].value.toInt()))
      }
      val exit = Position(GRID_SIZE)
      val queue = ArrayDeque<GridCell>()
      queue.add(
        GridCell(Position(0, 0), value = 0)
      )
      var steps = Int.MAX_VALUE
      val visited = mutableMapOf<DirectedPosition, Int>()
      while (queue.size > 0) {
        val curr = queue.removeFirst()
        if (curr.pos == exit) {
          if (curr.value < steps) {
            steps = curr.value
          }
          continue
        }
        GridUtils.cardinalDirections.forEach { direction ->
          val nextPos = Position(curr.pos.row + direction.y, curr.pos.col + direction.x)
          val positionDir = DirectedPosition(nextPos, direction)
          val newSum = curr.value + 1
          val newCell = GridCell(nextPos, direction, value = newSum)

          // Make sure new cell is in grid bounds, is not hitting an edge
          // and is not already visited
          if (GridUtils.isCellWithinGridBounds(
              GRID_SIZE + 1,
              newCell
            ) && !blockedPaths.contains(nextPos) && !visited.contains(
              positionDir
            )
          ) {
            visited[positionDir] = newSum
            queue.add(
              newCell
            )
          }
        }
      }
      println("Result: $steps")
    }

    fun solvePartTwo() {
      val input = Utils.readText("/inputs/day18.txt") ?: return
      val nums = NUMBER_REGEX.findAll(input).toList()
      val blockedPaths = mutableSetOf<Position>()
      // Try reaching the end again after each blockage
      for (i in nums.indices step 2) {
        // Doing y before x
        val position = Position(nums[i + 1].value.toInt(), nums[i].value.toInt())
        blockedPaths.add(position)

        val exit = Position(GRID_SIZE)
        val queue = ArrayDeque<GridCell>()
        queue.add(
          GridCell(Position(0, 0))
        )
        val visited = mutableSetOf<DirectedPosition>()
        var hasExit = false
        while (queue.size > 0) {
          val curr = queue.removeFirst()
          // No point continuing the loop if we've found the exit
          if (curr.pos == exit) {
            hasExit = true
            break
          }
          GridUtils.cardinalDirections.forEach { direction ->
            val nextPos = Position(curr.pos.row + direction.y, curr.pos.col + direction.x)
            val positionDir = DirectedPosition(nextPos, direction)
            val newCell = GridCell(nextPos, direction)

            // Make sure new cell is in grid bounds, is not hitting an edge
            // and is not already visited
            if (GridUtils.isCellWithinGridBounds(
                GRID_SIZE + 1,
                newCell
              ) && !blockedPaths.contains(nextPos) && !visited.contains(
                positionDir
              )
            ) {
              visited.add(positionDir)
              queue.add(
                newCell
              )
            }
          }
        }
        // We've found the first byte that blocks the exit
        if (!hasExit) {
          // Result order is reversed due to position class using y before x
          println("Result: $position")
          break
        }
      }
    }
  }
}

fun main() {
  Day18.solvePartTwo()
}