package `2024`

import shared.kotlin.*

class Day21 {
  companion object {
    private val posToPadKey =
      hashMapOf(
        Position(0, 0) to "7",
        Position(0, 1) to "8",
        Position(0, 2) to "9",
        Position(1, 0) to "4",
        Position(1, 1) to "5",
        Position(1, 2) to "6",
        Position(2, 0) to "1",
        Position(2, 1) to "2",
        Position(2, 2) to "3",
        Position(3, 1) to "0",
        Position(3, 2) to "A"
      )

    private val posToDirectionArrowsMap = hashMapOf(
      Position(0, 1) to "^",
      Position(0, 2) to "A",
      Position(1, 0) to "<",
      Position(1, 1) to "v",
      Position(1, 2) to ">",
    )

    private val directionToArrowsMap = hashMapOf(
      Direction(-1, 0) to "^",
      Direction(100, 100) to "A",
      Direction(0, -1) to "<",
      Direction(1, 0) to "v",
      Direction(0, 1) to ">",
    )

    private val directionPaths = getPaths(posToDirectionArrowsMap)
    private val numberPaths = getPaths(posToPadKey)

    private fun getPaths(positions: HashMap<Position, String>): MutableMap<String, HashMap<String, MutableSet<String>>> {
      val paths = mutableMapOf<String, HashMap<String, MutableSet<String>>>()

      for (start in positions) {
        for (end in positions) {
          if (start == end) {
            (paths.getOrPut(start.value) { hashMapOf() })[end.value] =
              mutableSetOf("")
            continue
          }

          val queue = ArrayDeque<GridCellWithData<List<Direction>>>()
          val base = GridCellWithData<List<Direction>>(start.key, data = listOf())
          queue.add(base)
          val visited = mutableMapOf<Pair<Position, DirectedPosition>, Int>()
          while (queue.size > 0) {
            val curr = queue.removeFirst()
            if (curr.pos == end.key) {
              val currentSet = (paths.getOrPut(start.value) { hashMapOf() })[end.value]
              if (currentSet != null) {
                currentSet.add(curr.data.joinToString("") { directionToArrowsMap[it]!! })
              } else {
                (paths.getOrPut(start.value) { hashMapOf() })[end.value] =
                  mutableSetOf(curr.data.joinToString("") { directionToArrowsMap[it]!! })
              }
              continue
            }
            GridUtils.cardinalDirections.forEach { direction ->
              val nextPos = curr.pos + direction
              val positionDir = DirectedPosition(nextPos, direction)
              val newCell = GridCellWithData(nextPos, direction, data = curr.data + direction)

              if (positions.contains(nextPos) && (!visited.contains(start.key to positionDir)
                        // Don't have enough variations without this
                        || newCell.data.size <= visited[start.key to positionDir]!!)
              ) {
                visited[start.key to positionDir] = newCell.data.size
                queue.add(
                  newCell
                )
              }
            }
          }
        }
      }

      return paths
    }

    // Took from another solution while debugging my own, this is much cleaner anyway
    private fun getCost(
      input: String,
      level: Int,
      paths: MutableMap<String, HashMap<String, MutableSet<String>>> = numberPaths,
      cache: MutableMap<Pair<String, Int>, Long> = mutableMapOf()
    ): Long {
      return cache.getOrPut(input to level) {
        if (level == 0) input.length.toLong() else
          "A$input".zipWithNext().sumOf { (from, to) ->
            paths[from.toString()]!![to.toString()]!!.minOf { possiblePath ->
              getCost("${possiblePath}A", level - 1, directionPaths, cache)
            }
          }
      }
    }

    fun solvePartOne() {
      val input = Utils.readLines("/inputs/2024/day21.txt") ?: return
      var complexitiesSum = 0L
      for (code in input) {
        val totalDirections = getCost(code, 26)
        val numericPartOfCode = code.replace("A", "").toInt()
        complexitiesSum += totalDirections * numericPartOfCode
      }
      println("Result: $complexitiesSum")
    }
  }
}

fun main() {
  Day21.solvePartOne()
}