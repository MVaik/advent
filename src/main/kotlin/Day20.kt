import shared.GridCell
import shared.GridUtils
import shared.GridUtils.Companion.cardinalDirectionOpposites
import shared.GridUtils.Companion.cardinalDirections
import shared.GridUtils.Companion.walkGrid
import shared.Position
import shared.Utils
import kotlin.math.abs

class Day20 {
  companion object {


    fun solvePartOne() {
      val input = Utils.readLines("/inputs/day20.txt") ?: return
      var base: GridCell? = null
      val walls = mutableSetOf<Position>()
      val visited = mutableSetOf<Position>()
      input.walkGrid { char, row, col, grid ->
        if (char == '#') {
          walls.add(Position(row, col))
        }
        if (char != 'S') {
          return@walkGrid
        }
        val queue = ArrayDeque<GridCell>()
        queue.add(
          GridCell(Position(row, col), value = 0, path = listOf())
        )
        while (queue.size > 0) {
          val curr = queue.removeFirst()
          if (grid[curr.pos.row][curr.pos.col] == 'E') {
            base = curr
            break
          }
          cardinalDirections.forEach { direction ->
            val nextPos = Position(curr.pos.row + direction.y, curr.pos.col + direction.x)
            val newSum = curr.value + 1
            // Keep track of previous path
            val newCell =
              GridCell(
                nextPos,
                direction,
                value = newSum,
                path = curr.addToPath(curr),
              )
            // Make sure new cell is in grid bounds, is not hitting an edge
            // and either is not already visited or is cheaper than the visited one
            if (GridUtils.isCellWithinGridBounds(
                grid,
                newCell
              ) && grid[nextPos.row][nextPos.col] != '#' && !visited.contains(
                nextPos
              )
            ) {
              queue.add(
                newCell
              )
              visited.add(nextPos)
            }
          }
        }
      }

      val cheats = walls.mapNotNull { wall ->
        // Get pairs that have a path on the opposite side
        val savedTime = cardinalDirections.mapNotNull { direction ->
          val nextPos = Position(wall.row + direction.y, wall.col + direction.x)
          val oppositeDir = cardinalDirectionOpposites[direction]
          val oppositePos = Position(wall.row + oppositeDir!!.y, wall.col + oppositeDir.x)
          if (visited.contains(nextPos) && visited.contains(oppositePos)) nextPos else null
        }
        if (savedTime.size == 2) savedTime else null
      }
        .map { cheat ->
          // Make sure cheat starts and ends on the path
          var firstPosSteps = base?.path?.find { cell -> cell.pos == cheat.first() }
          var secondPosSteps = base?.path?.find { cell -> cell.pos == cheat.last() }
          if (firstPosSteps == null && base!!.pos == cheat.first()) {
            firstPosSteps = base
          }
          if (secondPosSteps == null && base!!.pos == cheat.last()) {
            secondPosSteps = base
          }

          abs(secondPosSteps!!.value - firstPosSteps!!.value) - 2
        }.groupingBy { it }.eachCount()
      val cheatsSavingOverHundred = cheats.entries.fold(0) { acc, cheat ->
        if (cheat.key >= 100) acc + cheat.value else acc
      }
      println("Result: $cheatsSavingOverHundred")
    }


    fun solvePartTwo() {
      val input = Utils.readLines("/inputs/day20.txt") ?: return
      var base: GridCell? = null
      val visited = mutableSetOf<Position>()
      // Get uncheated path
      input.walkGrid { char, row, col, grid ->
        if (char != 'S') {
          return@walkGrid
        }
        val queue = ArrayDeque<GridCell>()
        queue.add(
          GridCell(Position(row, col), value = 0, path = listOf())
        )
        while (queue.size > 0) {
          val curr = queue.removeFirst()
          if (grid[curr.pos.row][curr.pos.col] == 'E') {
            base = curr
            break
          }
          cardinalDirections.forEach { direction ->
            val nextPos = Position(curr.pos.row + direction.y, curr.pos.col + direction.x)
            val newSum = curr.value + 1
            // Keep track of previous path
            val newCell =
              GridCell(
                nextPos,
                direction,
                value = newSum,
                path = curr.addToPath(curr),
              )
            // Make sure new cell is in grid bounds, is not hitting an edge
            // and either is not already visited or is cheaper than the visited one
            if (GridUtils.isCellWithinGridBounds(
                grid,
                newCell
              ) && grid[nextPos.row][nextPos.col] != '#' && !visited.contains(
                nextPos
              )
            ) {
              queue.add(
                newCell
              )
              visited.add(nextPos)
            }
          }
        }
      }

      // Turn base path into a map of times
      val allPaths = (base!!.path!! + listOf(base!!)).associateBy({ it.pos }, { it.value })
      val cheats = hashMapOf<Pair<Position, Position>, Int>()
      for (path in allPaths) {
        for (cheatDuration in 2..20) {
          for (i in 0..cheatDuration) {
            // Get all possible neighbor variations
            listOf(
              Position(path.key.row + i, path.key.col - cheatDuration + i),
              Position(path.key.row + cheatDuration - i, path.key.col + i),
              Position(path.key.row - i, path.key.col + cheatDuration - i),
              Position(path.key.row - cheatDuration + i, path.key.col - i)
            ).forEach { neighbor ->
              if (allPaths.containsKey(neighbor)) {
                val savedTime = allPaths[neighbor]!! - path.value - cheatDuration
                if (savedTime >= 100) {
                  // If the neighbor is a valid point on the path, save it along with the saved time
                  cheats[Pair(path.key, neighbor)] = allPaths[neighbor]!! - path.value - cheatDuration
                }
              }
            }
          }
        }
      }
      // Group cheats by their saved time, saving count as value, then sum up the counts
      val validCheats =
        cheats.entries.groupingBy { it.value }.eachCount().toList().fold(0) { acc, cheat -> acc + cheat.second }

      println("Result: $validCheats")
    }
  }
}

fun main() {
  Day20.solvePartTwo()
}