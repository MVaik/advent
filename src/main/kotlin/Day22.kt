import shared.Utils

class Day22 {
  companion object {
    private fun calcNextSecretNumber(number: Long): Long {
      var num = number
      num = num xor num * 64
      num %= 16777216

      num = num xor num / 32
      num %= 16777216

      num = num xor num * 2048
      num %= 16777216
      return num
    }

    private fun calcNthSecretNumber(startNumber: Long, iterations: Int): Long {
      var num = startNumber
      for (i in 0..<iterations) {
        num = calcNextSecretNumber(num)
      }
      return num
    }

    fun solvePartOne() {
      val input = Utils.readLines("/inputs/day22.txt")?.map { it.toLong() } ?: return
      val sum = input.fold(0L) { acc, value -> acc + calcNthSecretNumber(value, 2000) }
      println("Result: $sum")
    }

    fun solvePartTwo() {
      val input = Utils.readLines("/inputs/day22.txt")?.map { it.toLong() } ?: return
      val prices = input.map { baseVal ->
        // Create list of lists containing secret number, price and diff from prev price
        val data = mutableListOf(listOf(baseVal, baseVal % 10, null))
        // Create 2000 iterations with prices and diffs
        (1..2000).forEach { i ->
          if (data[i - 1][0] != null) {
            val newValue = calcNextSecretNumber(data[i - 1][0]!!)
            val newValuePrice = newValue % 10
            data.add(listOf(newValue, newValuePrice, newValuePrice - data[i - 1][1]!!))
          }
        }
        data
      }
      // Using sequence list as key as list order is checked in key equality, something like a set does not work
      val priceDiffs = hashMapOf<List<Long>, HashMap<Int, Long>>()
      for (priceIndex in prices.indices) {
        // Start counting sequences from the fifth as the first one doesn't have a diff
        for (i in 4..<prices[priceIndex].size) {
          val sequence = mutableListOf<Long>()
          for (j in i - 3..i) {
            sequence.add(prices[priceIndex][j][2]!!)
          }
          if (priceDiffs[sequence] != null) {
            // Only add price if it doesn't already exist for this sequence and buyer
            if (priceDiffs[sequence]!![priceIndex] == null) {
              priceDiffs[sequence]!![priceIndex] = prices[priceIndex][i][1]!!
            }
          } else {
            priceDiffs[sequence] = hashMapOf(priceIndex to prices[priceIndex][i][1]!!)
          }
        }
      }

      val max = priceDiffs.values.maxOf { it.values.fold(0L) { acc, price -> acc + price } }
      println("Result: $max")
    }
  }
}

fun main() {
  Day22.solvePartTwo()
}