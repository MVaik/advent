package `2024`

import shared.kotlin.Utils

class Day9 {
  companion object {
    fun fragmentDisk() {
      val input = Utils.readText("/inputs/2024/day9.txt") ?: return
      val space = ArrayList<Int>()
      for (i in input.indices) {
        val num = input[i].digitToInt()
        val id = if (i % 2 == 0) i / 2 else -1
        // Add the id based on the num size instead of just once
        // This creates multiple spots for each id, which can be partially filled later
        for (j in 0..<num) {
          space.add(id)
        }
      }
      var spaceStart = 0
      var spaceEnd = space.size - 1
      val compactedFiles = ArrayList<Int>()
      // Move files from end until not possible anymore
      while (spaceStart <= spaceEnd) {
        val curr = space[spaceStart]
        if (curr == -1) {
          while (spaceEnd >= 0) {
            spaceEnd--
            if (space[spaceEnd + 1] != -1) {
              compactedFiles.add(space[spaceEnd + 1])
              break
            }
          }
        } else {
          compactedFiles.add(curr)
        }
        spaceStart++
      }
      val checkSum = compactedFiles.foldIndexed(0L) { index, acc, position ->
        acc + (position * index)
      }
      println("Result: %s".format(checkSum))
    }

    fun fragmentDiskAsBlocks() {
      val input = Utils.readText("/inputs/2024/day9.txt") ?: return
      val space = mutableListOf<Pair<Int, Int>>()
      for (i in input.indices) {
        val num = input[i].digitToInt()
        val id = if (i % 2 == 0) i / 2 else -1
        space.add(Pair(num, id))
      }
      var spaceStart = 0
      val usedPairs = mutableSetOf<Pair<Int, Int>>()
      while (spaceStart <= space.size - 1) {
        val curr = space[spaceStart]
        if (curr.second == -1) {
          var spaceAvailable = curr.first
          // Find a match that has not been used yet and fits into the empty space
          val index =
            space.indexOfLast { possibleMatch ->
              possibleMatch.second != -1 && possibleMatch.first <= spaceAvailable && !usedPairs.contains(
                possibleMatch
              )
            }
          if (index != -1 && index > spaceStart) {
            val match = space[index]
            usedPairs.add(match)
            spaceAvailable -= match.first
            // Move item around in place
            space[spaceStart] = match
            // Replace used item with empty space
            space[index] = Pair(match.first, -1)
            if (spaceAvailable > 0) {
              // Add more empty space after our filled space if there's leftover
              space.add(spaceStart + 1, Pair(spaceAvailable, -1))
            }
          }
        }
        spaceStart++
      }
      val sum = space.fold(ArrayList<Long>()) { acc, item ->
        for (i in 0..<item.first) {
          // Add value as a long to prevent overflows during multiplication
          acc.add(item.second.toLong())
        }
        acc
      }.reduceIndexed { index, acc, num ->
        // Only multiply values that are above 0, the rest are only there to pad index,
        // which subsequently increase multipliers for others
        acc + if (num >= 0) index * num else 0
      }

      println("Result: %s".format(sum))
    }
  }
}

fun main(args: Array<String>) {
  Day9.fragmentDiskAsBlocks()
}