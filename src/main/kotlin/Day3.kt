import shared.Utils

class Day3 {
  companion object {
    // Match valid multiplication values or operators
    private val validMultiplicationRegex =
      Regex(
        "mul\\((?<firstNum>\\d{0,3}),(?<secondNum>\\d{0,3})\\)|(?<operator>do\\(\\)|don't\\(\\))"
      )

    fun addMultiplications(enableOperators: Boolean) {
      val input = Utils.readText("/inputs/day3.txt")
      var result = 0
      if (input != null) {
        val matches = validMultiplicationRegex.findAll(input)
        // Multiplication is enabled by default
        var multiplicationEnabled = true
        for (match in matches) {
          val operator = match.groups["operator"]
          // If we the current match is an operator, set the boolean and skip to next iteration
          if (operator != null && enableOperators) {
            multiplicationEnabled = operator.value == "do()"
            continue
          }
          val firstNum = match.groups["firstNum"]
          val secondNum = match.groups["secondNum"]
          // Add to result if multiplication is enabled
          if (firstNum != null && secondNum != null && multiplicationEnabled) {
            result += Integer.parseInt(firstNum.value) * Integer.parseInt(secondNum.value)
          }
        }
      }
      println("Result: %s".format(result))
    }
  }
}

fun main(args: Array<String>) {
  Day3.addMultiplications(true)
}
