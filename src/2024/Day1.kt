package `2024`

import kotlin.math.abs


class Day1 {

  companion object {
    private var inputDelimiter = "   "
    fun readInput(): Pair<ArrayList<Int>, ArrayList<Int>> {
      val inputStream = this::class.java.getResourceAsStream("/inputs/2024/day1.txt")?.bufferedReader()?.readLines()
      val firstArr = ArrayList<Int>()
      val secondArr = ArrayList<Int>()
      inputStream?.forEach {
        val (first, second) = it.split(inputDelimiter)
        firstArr.add(Integer.parseInt(first))
        secondArr.add(Integer.parseInt(second))
      }
      return Pair(firstArr, secondArr)
    }

    fun calcDistances() {
      val (firstArr, secondArr) = readInput()
      firstArr.sort()
      secondArr.sort()
      var sum = 0
      for (i in firstArr.indices) {
        sum += abs(firstArr[i] - secondArr[i])
      }
      println("Result: %s".format(sum))
    }

    fun calcSimilarity() {
      val (firstArr, secondArr) = readInput()
      val secondArrOccurrences = HashMap<Int, Int>()
      for (i in secondArr) {
        if (secondArrOccurrences.containsKey(i)) {
          secondArrOccurrences[i] = secondArrOccurrences[i]!! + 1
        } else {
          secondArrOccurrences[i] = 1
        }
      }
      var similaritySum = 0
      for (i in firstArr) {
        similaritySum += secondArrOccurrences[i]?.times(i) ?: 0
      }
      println("Result: %s".format(similaritySum))
    }
  }
}

fun main(args: Array<String>) {
}
