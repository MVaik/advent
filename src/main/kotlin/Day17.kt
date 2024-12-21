import shared.Utils
import shared.Utils.Companion.NUMBER_REGEX
import shared.Utils.Companion.ePrint
import kotlin.math.pow

class Day17 {
  companion object {

    private fun getComboOperand(operand: Long, a: Long, b: Long, c: Long): Long {
      return when (operand) {
        0L, 1L, 2L, 3L -> operand
        4L -> a
        5L -> b
        else -> c
      }
    }

    private fun getOutput(numbers: List<Long>, registers: HashMap<Int, Long>): List<Long> {
      var pointer = 0L
      val output = mutableListOf<Long>()
      var a = registers[0]!!
      var b = registers[1]!!
      var c = registers[2]!!
      while (pointer < numbers.size) {
        val code = numbers[pointer.toInt()]
        val operandNum = numbers[pointer.toInt() + 1]
        when (code) {
          0L -> {
            a /= 2.0.pow(getComboOperand(operandNum, a, b, c).toDouble()).toLong()
          }

          1L -> {
            b = b.xor(operandNum)
          }

          2L -> {
            b = getComboOperand(operandNum, a, b, c) % 8
          }

          3L -> {
            if (a != 0L) {
              pointer = operandNum
              continue
            }
          }

          4L -> {
            b = b.xor(c)
          }

          5L -> {
            val outputNum = getComboOperand(operandNum, a, b, c) % 8
            output.add(outputNum)
          }

          6L -> {
            b = a / 2.0.pow(getComboOperand(operandNum, a, b, c).toDouble()).toLong()
          }

          7L -> {
            c = a / 2.0.pow(getComboOperand(operandNum, a, b, c).toDouble()).toLong()
          }
        }
        pointer += 2
      }
      return output
    }


    fun solvePartOne() {
      val input = Utils.readText("/inputs/day17.txt") ?: return
      val registers = HashMap<Int, Long>()
      val numbers = NUMBER_REGEX.findAll(input).map { it.value.toLong() }.toList()
      for (num in 0..<3) {
        registers[num] = numbers[num]
      }
      val restOfNumbers = numbers.subList(3, numbers.size)
      val output = getOutput(restOfNumbers, registers)
      println("Result: $output")
    }


    fun solvePartTwo() {
      val input = Utils.readText("/inputs/day17.txt") ?: return
      val registers = HashMap<Int, Long>()
      val numbers = NUMBER_REGEX.findAll(input).map { it.value.toLong() }.toList()
      for (num in 0..<3) {
        registers[num] = 0L
      }

      val restOfNumbers = numbers.subList(3, numbers.size)

      val queue = ArrayDeque<Pair<Long, Int>>()
      queue.add(Pair(0L, 1))
      val possibleResults = mutableSetOf<Long>()
      while (queue.size > 0) {
        val curr = queue.removeFirst()
        for (i in 0..<8) {
          // Bit shifting magic, last 3 bits determine output
          val newA = (curr.first shl 3) or i.toLong()
          registers[0] = newA
          val output = getOutput(restOfNumbers, registers)
          if (output == restOfNumbers) {
            possibleResults.add(newA)
          } else if (output == restOfNumbers.subList(restOfNumbers.size - curr.second, restOfNumbers.size)) {
            queue.add(Pair(newA, curr.second + 1))
          }
        }
      }
      val result = possibleResults.min()
      ePrint(result)
    }
  }
}

fun main() {
  Day17.solvePartTwo()
}