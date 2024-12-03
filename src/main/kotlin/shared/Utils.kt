package shared

class Utils {
  companion object {
    fun readLines(path: String): List<String>? {
      return this::class.java.getResourceAsStream(path)?.bufferedReader()?.readLines()
    }

    fun readText(path: String): String? {
      return this::class.java.getResourceAsStream(path)?.bufferedReader()?.readText()
    }
  }
}