package shared

class Utils {
  companion object {
    val cardinalDirections = listOf(
      Pair(-1, 0), Pair(0, 1),
      Pair(1, 0), Pair(0, -1)
    )

    fun readLines(path: String): List<String>? {
      return this::class.java.getResourceAsStream(path)?.bufferedReader()?.readLines()
    }

    fun readText(path: String): String? {
      return this::class.java.getResourceAsStream(path)?.bufferedReader()?.readText()
    }

    fun isPairWithinGridBounds(grid: List<String>, pos: Pair<Int, Int>): Boolean {
      return pos.first >= 0 && pos.first < grid.size && pos.second >= 0 && pos.second < grid[0].length
    }
  }
}