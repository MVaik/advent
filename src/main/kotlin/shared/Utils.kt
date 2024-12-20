package shared

class Utils {
  companion object {
    val NUMBER_REGEX = Regex("\\d+")

    fun readLines(path: String): List<String>? {
      return this::class.java.getResourceAsStream(path)?.bufferedReader()?.readLines()
    }

    fun readText(path: String): String? {
      return this::class.java.getResourceAsStream(path)?.bufferedReader()?.readText()
    }

    fun printLn(vararg input: Any) {
      println(input.joinToString(", "))
    }
  }
}