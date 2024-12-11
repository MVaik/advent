import shared.Utils

class Day11 {
  companion object {
    fun watchStones(blinkCount: Int) {
      val input = Utils.readText("/inputs/day11.txt") ?: return
      var nums = input.split(" ").fold(HashMap<String, Long>()) { acc, num ->
        acc[num] = acc.getOrDefault(num, 0) + 1
        acc
      }
      for (i in 0..<blinkCount) {
        val newNums = HashMap<String, Long>()
        for ((value) in nums) {
          when {
            value.toLong() == 0L -> {
              newNums["1"] = newNums.getOrDefault("1", 0L) + nums[value]!!
            }

            value.length % 2 == 0 -> {
              val halfwayPoint = value.length / 2
              val firstHalf = value.slice(0..<halfwayPoint)
              val secondHalf = value.slice(halfwayPoint..<value.length).toLong().toString()
              newNums[firstHalf] = newNums.getOrDefault(firstHalf, 0L) + nums[value]!!
              newNums[secondHalf] = newNums.getOrDefault(secondHalf, 0L) + nums[value]!!
            }

            else -> {
              val newVal = (value.toLong() * 2024L).toString()
              newNums[newVal] = newNums.getOrDefault(newVal, 0L) + nums[value]!!
            }
          }
        }
        nums = newNums
      }
      val count = nums.values.fold(0L) { acc, c ->
        acc + c
      }
      println("Result: %s".format(count))
    }
  }
}

fun main(args: Array<String>) {
  Day11.watchStones(75)
}

