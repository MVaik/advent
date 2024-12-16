package shared

data class Position(val row: Int, val col: Int)
data class Direction(val y: Int, val x: Int)

data class DirectedPosition(val position: Position, val direction: Direction)


class GridCell(
  val pos: Position,
  val direction: Direction = Direction(0, 0),
  val path: List<GridCell>? = null,
  val value: Int = -1
) {
  fun addToPath(element: GridCell): List<GridCell> {
    if (path != null) {
      return path.plus(element)
    }
    return listOf(element)
  }
}

class GridUtils {
  companion object {
    val cardinalDirectionPairs = listOf(
      Pair(-1, 0), Pair(0, 1),
      Pair(1, 0), Pair(0, -1)
    )
    val cardinalDirections = listOf(
      Direction(-1, 0), Direction(0, 1),
      Direction(1, 0), Direction(0, -1)
    )
    val cardinalDirectionOpposites =
      mapOf(Pair(-1, 0) to Pair(1, 0), Pair(1, 0) to Pair(-1, 0), Pair(0, -1) to Pair(0, 1), Pair(0, 1) to Pair(0, -1))

    @JvmName("CellWithinBoundsStringList")
    fun isCellWithinGridBounds(grid: List<String>, cell: GridCell): Boolean {
      return cell.pos.row >= 0 && cell.pos.row < grid.size && cell.pos.col >= 0 && cell.pos.col < grid[0].length
    }

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