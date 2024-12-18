import shared.Utils
import shared.Utils.Companion.NUMBER_REGEX
import kotlin.math.pow

class Day17 {
  companion object {

    fun getComboOperand(operand: Long, registers: HashMap<Int, Long>): Long {
      return when (operand) {
        0L, 1L, 2L, 3L -> operand
        4L -> registers[0]!!
        5L -> registers[1]!!
        else -> registers[2]!!
      }
    }

    fun getOutput(numbers: List<Long>, registers: HashMap<Int, Long>, expectedFirstOutput: Long? = null): String {
      var pointer = 0L
      var output = ""
      while (pointer < numbers.size) {
        val code = numbers[pointer.toInt()]
        val operandNum = numbers[pointer.toInt() + 1]
        when (code) {
          0L -> {
            registers[0] = registers[0]!! / 2.0.pow(getComboOperand(operandNum, registers).toDouble()).toLong()
          }

          1L -> {
            registers[1] = registers[1]!!.xor(operandNum)
          }

          2L -> {
            registers[1] = getComboOperand(operandNum, registers) % 8
          }

          3L -> {
            if (registers[0] != 0L) {
              pointer = operandNum
              continue
            }
          }

          4L -> {
            registers[1] = registers[1]!!.xor(registers[2]!!)
          }

          5L -> {
            val outputNum = getComboOperand(operandNum, registers) % 8
            output += "%s,".format(outputNum)
            if (expectedFirstOutput != null && output.isEmpty() && outputNum != expectedFirstOutput) {
              break
            }
          }

          6L -> {
            registers[1] = registers[0]!! / 2.0.pow(getComboOperand(operandNum, registers).toDouble()).toLong()
          }

          7L -> {
            registers[2] = registers[0]!! / 2.0.pow(getComboOperand(operandNum, registers).toDouble()).toLong()
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
        registers[num] = numbers[num]
      }
      val restOfNumbers = numbers.subList(3, numbers.size)
      val numbersString = restOfNumbers.joinToString(",") + ","
      var num = 0L
      while (true) {
        registers[0] = num
        val output = getOutput(restOfNumbers, registers, restOfNumbers[0])
        if (output == numbersString) {
          break
        }
        num++
      }
      println("Result: $num")
    }
  }
}

fun main() {
  Day17.solvePartTwo()

}