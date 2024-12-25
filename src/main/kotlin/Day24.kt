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

    fun solvePartOneAndTwo() {
      val (values, transformations) = getInput() ?: return
      val queue = ArrayDeque<List<String>>()
      for (transformation in transformations) {
        queue.add(transformation)
      }
      val orInputs = mutableSetOf<String>()
      val faultyXorOperations = mutableSetOf<List<String>>()
      val faultyZOutputs = mutableSetOf<List<String>>()
      val andOutputs = mutableSetOf<List<String>>()
      val xorInputs = mutableSetOf<String>()
      val xorOutputs = mutableSetOf<String>()
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
            andOutputs.add(curr)
            if (curr[3].startsWith("z") && curr[3] != "z45") {
              faultyZOutputs.add(curr)
            }
          }

          "OR" -> {
            values[curr[3]] = if (firstValue == 1 || secondValue == 1) 1 else 0
            orInputs.addAll(listOf(curr[0], curr[2]))
            if (curr[3].startsWith("z") && curr[3] != "z45") {
              faultyZOutputs.add(curr)
            }
          }

          "XOR" -> {
            values[curr[3]] = if (firstValue != secondValue) 1 else 0
            val isValidInput = listOf(curr[0], curr[2]).all { it.startsWith("x") || it.startsWith("y") }
            val isValidOutput = curr[3].startsWith("z")
            xorInputs.addAll(listOf(curr[0], curr[2]))
            if (!isValidInput && !isValidOutput) {
              faultyXorOperations.add(curr)
            } else if (isValidInput && listOf(curr[0], curr[2], curr[3]).none { it.endsWith("00") }) {
              xorOutputs.add(curr[3])
            }
          }
        }
      }
      val valueFromBits =
        values.filterKeys { key -> key.startsWith("z") }.toList().sortedByDescending { it.first }
          .joinToString("") { it.second.toString() }.toLong(2)
      println("Result: $valueFromBits")

      val faultyAndOutputs = andOutputs.filter { outputRow ->
        listOf(outputRow[0], outputRow[2]).none { it.startsWith("x00") || it.startsWith("y00") } && !orInputs.contains(
          outputRow[3]
        )
      }
      val faultyXorOutputs = xorOutputs.filter { output -> !xorInputs.contains(output) }
      val faultyWires = listOf(faultyAndOutputs.map { it[3] },
        faultyXorOperations.map { it[3] },
        faultyXorOutputs,
        faultyZOutputs.map { it[3] }).flatten().toSortedSet().joinToString(",")
      println("Faulty wires: $faultyWires")
    }
  }
}

fun main() {
  Day24.solvePartOneAndTwo()
}