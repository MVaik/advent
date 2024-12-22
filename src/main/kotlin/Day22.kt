import shared.Utils

class Day22 {
  companion object {
    private fun calcNthSecretNumber(startNumber: Long, iterations: Int): Long {
      var num = startNumber
      for (i in 0..<iterations) {
        num = num xor num * 64
        num %= 16777216

        num = num xor num / 32
        num %= 16777216

        num = num xor num * 2048
        num %= 16777216
      }
      return num
    }

    fun solvePartOne() {
      val input = Utils.readLines("/inputs/day22.txt")?.map { it.toLong() } ?: return
      val sum = input.fold(0L) { acc, value -> acc + calcNthSecretNumber(value, 2000) }
      println("Result: $sum")
    }
  }
}

fun main() {
  Day22.solvePartOne()
}