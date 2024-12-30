package `2024`

import kotlin.math.abs
import kotlin.streams.toList

class Day2 {
  companion object {
    private var inputDelimiter = " "
    private fun readInput(): ArrayList<List<Int>> {
      val inputStream = this::class.java.getResourceAsStream("/inputs/2024/day2.txt")?.bufferedReader()?.readLines()
      val result = ArrayList<List<Int>>()
      inputStream?.forEach {
        val parsedInts = it.split(inputDelimiter).stream().mapToInt(Integer::parseInt).toList()
        result.add(parsedInts)
      }
      return result
    }

    fun calcSafeReports() {
      val rows = readInput()
      var safeRows = 0
      for (i in rows) {
        var prevNum: Int? = null
        var prevDirection: Int? = null
        var isSafe = true
        for (j in i) {
          // Skip to next iteration if we don't have previous data yet
          if (prevNum == null) {
            prevNum = j
            continue
          }
          val direction: Int = if (j > prevNum) {
            1
          } else {
            -1
          }
          val diff = abs(j - prevNum)
          // Make sure the diff stays between 1-3 and direction doesn't change
          if (diff < 1 || diff > 3 || (prevDirection != null && prevDirection != direction)) {
            isSafe = false
            break
          }
          prevDirection = direction
          prevNum = j
        }
        if (isSafe) {
          safeRows++
        }
      }
      println("Result: %s".format(safeRows))
    }

    fun calcSafeReportsWithProblemDampener() {
      val rows = readInput()
      var safeRows = 0
      for (i in rows) {
        var prevNum: Int? = null
        var prevDirection: Int? = null
        var isSafe = true
        for (j in i.indices) {
          val num = i[j]
          // Skip to next iteration if we don't have previous data yet
          if (prevNum == null) {
            prevNum = num
            continue
          }
          val direction: Int = if (num > prevNum) {
            1
          } else {
            -1
          }
          val diff = abs(num - prevNum)
          // Make sure the diff stays between 1-3 and direction doesn't change
          if (diff < 1 || diff > 3 || (prevDirection != null && prevDirection != direction)) {
            isSafe = false
            break
          }
          prevDirection = direction
          prevNum = num
        }
        if (!isSafe) {
          // Brute force by checking if skipping any of the indices makes the row safe
          for (skipIndex in i.indices) {
            prevNum = null
            prevDirection = null
            var failed = false
            for (j in i.indices) {
              val num = i[j]
              if (j == skipIndex) {
                continue
              }
              // Skip to next iteration if we don't have previous data yet
              if (prevNum == null) {
                prevNum = num
                continue
              }
              val direction = if (num > prevNum) 1 else -1

              val diff = abs(num - prevNum)
              // Make sure the diff stays between 1-3 and direction doesn't change
              if (diff < 1 || diff > 3 || (prevDirection != null && prevDirection != direction)) {
                failed = true
                break
              }
              prevDirection = direction
              prevNum = num
            }
            // If loop didn't fail we can conclude that skipping this level makes the row safe
            if (!failed) {
              isSafe = true
              break
            }
          }
        }
        if (isSafe) {
          safeRows++
        }
      }
      println("Result: %s".format(safeRows))
    }
  }
}

fun main(args: Array<String>) {
}
