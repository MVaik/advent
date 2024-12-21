package shared

data class Position(val row: Int, val col: Int) {
  constructor(size: Int) : this(size, size)
}

data class Direction(val y: Int, val x: Int)

data class DirectedPosition(val position: Position, val direction: Direction)


open class GridCell(
  val pos: Position,
  val direction: Direction = Direction(0, 0),
  val path: List<GridCell>? = null,
  val value: Int = -1,
  val visited: MutableSet<Position>? = null
) {
  fun addToPath(element: GridCell): List<GridCell> {
    if (path != null) {
      return path.plus(element)
    }
    return listOf(element)
  }
}

class GridCellWithData<T>(
  pos: Position,
  direction: Direction = Direction(0, 0),
  var data: T,
  path: List<GridCell>? = null,
  value: Int = -1,
  visited: MutableSet<Position>? = null
) : GridCell(pos, direction, path, value, visited)

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
      mapOf(
        Direction(-1, 0) to Direction(1, 0),
        Direction(1, 0) to Direction(-1, 0),
        Direction(0, -1) to Direction(0, 1),
        Direction(0, 1) to Direction(0, -1)
      )
    val cardinalDirectionOppositePairs =
      mapOf(Pair(-1, 0) to Pair(1, 0), Pair(1, 0) to Pair(-1, 0), Pair(0, -1) to Pair(0, 1), Pair(0, 1) to Pair(0, -1))

    @JvmName("CellWithinBoundsStringList")
    fun isCellWithinGridBounds(grid: List<String>, cell: GridCell): Boolean {
      return cell.pos.row >= 0 && cell.pos.row < grid.size && cell.pos.col >= 0 && cell.pos.col < grid[0].length
    }

    @JvmName("CellWithinBoundsIntSize")
    fun isCellWithinGridBounds(gridSize: Int, cell: GridCell): Boolean {
      return cell.pos.row in 0..<gridSize && cell.pos.col in 0..<gridSize
    }

    @JvmName("PositionWithinBoundsStringList")
    fun isPositionWithinGridBounds(grid: List<String>, position: Position): Boolean {
      return position.row >= 0 && position.row < grid.size && position.col >= 0 && position.col < grid[0].length
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

    fun IntRange.walkImaginaryGrid(action: (rowIndex: Int, colIndex: Int) -> Unit) {
      for (row in this) {
        for (col in this) {
          action(row, col)
        }
      }
    }
  }
}