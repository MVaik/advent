package `2024`

import shared.GridUtils
import shared.Utils
import kotlin.math.abs

data class AreaAndSides(
  var area: Int = 0,
  val sides: HashMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>> = hashMapOf()
) {
  fun incrementArea() {
    area++
  }

  fun addPos(direction: Pair<Int, Int>, pos: Pair<Int, Int>) {
    if (sides[direction] != null) {
      sides[direction]?.add(pos)
    } else {
      sides[direction] = mutableSetOf(pos)
    }
  }

}

class Day12 {
  companion object {

    private fun countAreaAndPerimeter(
      grid: List<String>,
      position: Pair<Int, Int>,
      plotType: Char,
      visitedLocations: MutableSet<Pair<Int, Int>>,
      counts: HashMap<String, Int> = hashMapOf("area" to 0, "perimeter" to 0)
    ): HashMap<String, Int> {
      // If we've left either the grid or our active plot, we've found one of the perimeter plots
      if (!GridUtils.isPairWithinGridBounds(
          grid,
          position
        ) || grid[position.first][position.second] != plotType
      ) {
        counts["perimeter"] = counts["perimeter"]!! + 1
        return counts
      }
      // Avoid visiting same locations multiple times
      if (visitedLocations.contains(position)
      ) {
        return counts
      }
      counts["area"] = counts["area"]!! + 1
      visitedLocations.add(position)
      for (direction in GridUtils.cardinalDirectionPairs) {
        val newPos = Pair(position.first + direction.first, position.second + direction.second)
        countAreaAndPerimeter(grid, newPos, plotType, visitedLocations, counts)
      }
      return counts
    }

    private fun countAreaAndSides(
      grid: List<String>,
      position: Pair<Int, Int>,
      plotType: Char,
      visitedLocations: MutableSet<Pair<Int, Int>>,
      data: AreaAndSides = AreaAndSides(),
      prevPos: Pair<Int, Int>? = null,
    ): AreaAndSides {
      if (!GridUtils.isPairWithinGridBounds(
          grid,
          position
        ) || grid[position.first][position.second] != plotType
      ) {
        // Save all the directions along with current pos
        if (prevPos != null) {
          val direction = Pair(position.first - prevPos.first, position.second - prevPos.second)
          data.addPos(direction, position)
        }
        return data
      }
      if (visitedLocations.contains(position)
      ) {
        return data
      }
      data.incrementArea()
      visitedLocations.add(position)
      for (direction in GridUtils.cardinalDirectionPairs) {
        val newPos = Pair(position.first + direction.first, position.second + direction.second)
        countAreaAndSides(grid, newPos, plotType, visitedLocations, data, position)
      }
      return data
    }

    fun calculateFencePrice() {
      val input = Utils.readLines("/inputs/2024/day12_example.txt") ?: return
      val visitedLocations = mutableSetOf<Pair<Int, Int>>()
      var price = 0
      for (row in input.indices) {
        for (col in input[row].indices) {
          if (visitedLocations.contains(Pair(row, col))) {
            continue
          }
          val char = input[row][col]
          val pos = Pair(row, col)
          val count = countAreaAndPerimeter(
            input,
            pos,
            char,
            visitedLocations
          )
          price += count["area"]!! * count["perimeter"]!!
        }
      }
      println("Result: %s".format(price))
    }

    fun calculateFencePriceWithSides() {
      val input = Utils.readLines("/inputs/2024/day12.txt") ?: return
      val visitedLocations = mutableSetOf<Pair<Int, Int>>()
      var price = 0
      for (row in input.indices) {
        for (col in input[row].indices) {
          if (visitedLocations.contains(Pair(row, col))) {
            continue
          }
          val (area, sides) = countAreaAndSides(input, Pair(row, col), input[row][col], visitedLocations)
          val sortedValues = sides.map { entry ->
            // Sort values based on direction, if direction changed row then sort by row values first etc
            if (entry.key.first != 0) entry.value.sortedWith(
              compareBy(
                { it.first },
                { it.second })
            ) else entry.value.sortedWith(compareBy({ it.second }, { it.first }))
          }
          val sideCount = sortedValues.fold(0) { finalCount, sortedValue ->
            val sidesCount = sortedValue.foldIndexed(0) { index, acc, item ->
              var result = 0
              if (index > 0) {
                val prev = sortedValue[index - 1]
                val diff = Pair(abs(prev.first - item.first), abs(prev.second - item.second))
                // If the position differs from previous one by more than 1 coordinate, then we have another side
                if (diff.first + diff.second > 1) {
                  result++
                }
              }
              acc + result
            }
            // Add 1 to sidesCount because each one already counts as one side by default
            finalCount + sidesCount + 1
          }
          price += area * sideCount
        }
      }
      println("Result: %s".format(price))
    }
  }
}

fun main(args: Array<String>) {
  Day12.calculateFencePriceWithSides()
}

