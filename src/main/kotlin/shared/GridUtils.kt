package shared

class GridUtils {
  companion object {
    val cardinalDirections = listOf(
      Pair(-1, 0), Pair(0, 1),
      Pair(1, 0), Pair(0, -1)
    )

    @JvmName("PairWithinBoundsStringList")
    fun isPairWithinGridBounds(grid: List<String>, pos: Pair<Int, Int>): Boolean {
      return pos.first >= 0 && pos.first < grid.size && pos.second >= 0 && pos.second < grid[0].length
    }

    @JvmName("PairWithinBoundsStringNestedList")
    fun isPairWithinGridBounds(grid: List<List<String>>, pos: Pair<Int, Int>): Boolean {
      return pos.first >= 0 && pos.first < grid.size && pos.second >= 0 && pos.second < grid[0].size
    }

    @JvmName("PairWithinBoundsStringCharList")
    fun isPairWithinGridBounds(grid: List<List<Char>>, pos: Pair<Int, Int>): Boolean {
      return pos.first >= 0 && pos.first < grid.size && pos.second >= 0 && pos.second < grid[0].size
    }

    @JvmName("WalkGridNestedList")
    fun <T> List<List<T>>.walkGrid(action: (element: T, rowIndex: Int, colIndex: Int, grid: List<List<T>>) -> Unit) {
      for (row in this.indices) {
        for (col in this[row].indices) {
          action(this[row][col], row, col, this)
        }
      }
    }

    @JvmName("WalkGridStringList")
    fun List<String>.walkGrid(action: (element: Char, rowIndex: Int, colIndex: Int, grid: List<String>) -> Unit) {
      for (row in this.indices) {
        for (col in this[row].indices) {
          action(this[row][col], row, col, this)
        }
      }
    }

    fun List<String>.walkGrid(action: (element: Char, rowIndex: Int, colIndex: Int) -> Unit) {
      for (row in this.indices) {
        for (col in this[row].indices) {
          action(this[row][col], row, col)
        }
      }
    }
  }
}