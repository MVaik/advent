import shared.Utils
import kotlin.math.pow

class Day7 {
  companion object {
    fun sumTrueCalculations(includeConcat: Boolean = false) {
      val input = Utils.readLines("/inputs/day7.txt") ?: return
      var sum = 0L
      val operators = if (includeConcat) arrayOf('+', '*', "||") else arrayOf('+', '*')
      for (row in input) {
        val (expectedSum, numbersString) = row.split(": ")
        val numbers = numbersString.split(" ").map(Integer::parseInt)
        val combinationsCount = operators.size.toDouble().pow((numbers.size - 1).toDouble())
        val combinations = ArrayList<Long>()
        var current = 0L
        // Loop until all possible combinations are used
        for (i in 0..<combinationsCount.toInt()) {
          current += numbers[0]
          var num = i
          // Loop through all numbers besides the number we already have inside current
          for (j in 1..<numbers.size) {
            val operator = operators[num % operators.size]
            when (operator) {
              '+' -> current += numbers[j]
              '*' -> current *= numbers[j]
              // Set the value directly when doing concatenation
              else -> current = (current.toString() + numbers[j].toString()).toLong()
            }
            num /= operators.size
          }
          combinations.add(current)
          current = 0L
        }
        // Have to keep numbers as long as they can be quite big
        val parsedExpectedSum = expectedSum.toLong()
        // Add to sum if our combinations array contains the expected value
        if (combinations.contains(parsedExpectedSum)) {
          sum += parsedExpectedSum
        }
      }

      println("Result: %s".format(sum))
    }
  }
}

fun main(args: Array<String>) {
  Day7.sumTrueCalculations(true)
}
