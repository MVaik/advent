package `2024`

import shared.Utils

class Day5 {
  companion object {
    fun orderAndCountUpdates() {
      val input = Utils.readLines("/inputs/2024/day5.txt")
      var correctlyOrderedMiddleSum = 0
      var inCorrectlyOrderedMiddleSum = 0
      if (input != null) {
        val orders = ArrayList<String>()
        val sequences = ArrayList<List<String>>()
        var addingOrders = true
        for (line in input) {
          // Switch to gathering sequences after empty line
          if (line.isEmpty()) {
            addingOrders = false
            continue
          }
          if (addingOrders) {
            orders.add(line)
          } else {
            sequences.add(line.split(","))
          }
        }
        val orderMap = HashMap<String, MutableSet<String>>()
        // Keep track of what elements an element should go before
        orders.forEach {
          val (before, after) = it.split("|")
          if (orderMap.containsKey(before)) {
            orderMap[before]?.add(after)
          } else {
            orderMap[before] = mutableSetOf(after)
          }
        }
        for (row in sequences) {
          // Sort row based on whether one of the elements is in the set of the other element
          // In other words, whether first should be sorted before second or vice versa
          val sortedRowArray = row.sortedWith { first, second ->
            when {
              (orderMap.containsKey(first) && orderMap[first]?.contains(second) == true) -> -1
              (orderMap.containsKey(second) && orderMap[second]?.contains(first) == true) -> 1
              else -> 0
            }
          }
          val middleNum = Integer.parseInt(sortedRowArray[(sortedRowArray.size / 2)])
          // Does the unsorted row have the same elements in the same order as our sorted row?
          if (row == sortedRowArray) {
            correctlyOrderedMiddleSum += middleNum
          } else {
            inCorrectlyOrderedMiddleSum += middleNum
          }
        }
      }
      println("Correctly: %s".format(correctlyOrderedMiddleSum))
      println("Incorrectly: %s".format(inCorrectlyOrderedMiddleSum))
    }
  }
}

fun main(args: Array<String>) {
  Day5.orderAndCountUpdates()
}
