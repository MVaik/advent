import shared.Utils

class Day24 {
  companion object {
    private fun getInput(): Pair<MutableMap<String, Int>, List<List<String>>>? {
      val (initialValues, transformations) = Utils.readText("/inputs/day24.txt")?.split("\r\n\r\n")
        ?: return null
      val values = mutableMapOf<String, Int>()
      val parsedTransformations = mutableListOf<List<String>>()
      for (row in initialValues.split("\r\n").filter { it.isNotBlank() }) {
        val (key, value) = row.split(": ")
        values[key] = value.toInt()
      }
      for (row in transformations.split("\r\n")) {
        val transformation = row.replace(" -> ", " ").split(" ")
        parsedTransformations.add(transformation)
      }
      return Pair(values, parsedTransformations)
    }

    fun solvePartOne() {
      val (values, transformations) = getInput() ?: return
      val queue = ArrayDeque<List<String>>()
      for (transformation in transformations) {
        queue.add(transformation)
      }

      while (queue.size > 0) {
        val curr = queue.removeFirst()
        if (!values.contains(curr[0]) || !values.contains(curr[2])) {
          queue.add(curr)
          continue
        }
        val firstValue = values[curr[0]]
        val secondValue = values[curr[2]]
        when (curr[1]) {
          "AND" -> {
            values[curr[3]] = if (firstValue == 1 && firstValue == secondValue) 1 else 0
          }

          "OR" -> {
            values[curr[3]] = if (firstValue == 1 || secondValue == 1) 1 else 0
          }

          "XOR" -> {
            values[curr[3]] = if (firstValue != secondValue) 1 else 0
          }
        }
      }
      val valueFromBits =
        values.filterKeys { key -> key.startsWith("z") }.toList().sortedByDescending { it.first }
          .joinToString("") { it.second.toString() }.toLong(2)
      println("Result: $valueFromBits")
    }
  }
}

fun main() {
  Day24.solvePartOne()
}