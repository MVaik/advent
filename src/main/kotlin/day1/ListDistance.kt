package day1

import day1.ListDistance.Companion.readInput
import kotlin.math.abs


class ListDistance {

  companion object {
    private var inputDelimiter = "   "
    fun readInput(): Pair<ArrayList<Int>, ArrayList<Int>> {
      val inputStream = this::class.java.getResourceAsStream("/day1/input.txt")?.bufferedReader()?.readLines()
      val firstArr = ArrayList<Int>()
      val secondArr = ArrayList<Int>()
      inputStream?.forEach {
        val (first, second) = it.split(inputDelimiter)
        firstArr.add(Integer.parseInt(first))
        secondArr.add(Integer.parseInt(second))
      }
      return Pair(firstArr, secondArr)
    }
  }


}

fun main(args: Array<String>) {
  val (firstArr, secondArr) = readInput()
  firstArr.sort()
  secondArr.sort()
  var sum = 0
  for (i in firstArr.indices) {
    sum += abs(firstArr[i] - secondArr[i])
  }
  println("Result: %s".format(sum))
}
