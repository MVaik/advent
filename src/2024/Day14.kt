package `2024`

import shared.kotlin.Utils
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


class Day14 {
  companion object {
    private val robotCoordsRegex = Regex("-?\\d+")
    private const val WIDTH = 101
    private const val HEIGHT = 103
    private const val IGNORED_X = WIDTH / 2
    private const val IGNORED_Y = HEIGHT / 2

    private fun wrapCoord(input: Int, max: Int): Int {
      // Handle negative values
      return ((input % max) + max) % max
    }

    private fun getRobots(): List<ArrayList<Int>>? {
      val input = Utils.readLines("/inputs/2024/day14.txt") ?: return null
      return input.map { robot ->
        // Get all digits in the row
        val matches = robotCoordsRegex.findAll(robot)
        val values = ArrayList<Int>()
        for (match in matches) {
          values.add(match.value.toInt())
        }
        values
      }
    }

    private fun moveRobots(robots: List<List<Int>>, iterations: Int): List<Pair<Int, Int>> {
      return robots.map { robot ->
        val (x, y, xVel, yVel) = robot
        Pair(
          wrapCoord((x + xVel * iterations), WIDTH),
          wrapCoord((y + yVel * iterations), HEIGHT),
        )
      }
    }

    private fun calcSafetyFactor(robots: List<Pair<Int, Int>>): Int {
      // Filter out ignored coords
      return robots.filter { robot -> robot.first != IGNORED_X && robot.second != IGNORED_Y }.groupBy { robot ->
        // Group into 4 quadrants based on whether they're below ignore coords
        Pair(robot.first < IGNORED_X, robot.second < IGNORED_Y)
        // Multiply each robot count together
      }.values.fold(1) { acc, groupedRobots ->
        acc * groupedRobots.size
      }
    }


    fun solvePartOne() {
      val robots = getRobots() ?: return
      val movedRobots = moveRobots(robots, 100)
      val safetyFactor = calcSafetyFactor(movedRobots)
      println("Result: %s".format(safetyFactor))
    }

    fun drawRobotsToImage() {
      var robots = getRobots() ?: return
      var iteration = 0
      // Create new image for each iteration and have puny human manually scan through for image
      while (true) {
        val path = "src/main/resources/day14/image_$iteration.png"

        val image = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)
        val color = Color(255, 255, 255)
        for (robot in robots) {
          image.setRGB(robot[0], robot[1], color.rgb)
        }

        val imageFile = File(path)
        imageFile.getParentFile().mkdirs()
        try {
          ImageIO.write(image, "png", imageFile)
        } catch (e: IOException) {
          e.printStackTrace()
        }
        robots = robots.map { robot ->
          arrayListOf(
            wrapCoord(robot[0] + robot[2], WIDTH),
            wrapCoord(robot[1] + robot[3], HEIGHT),
            robot[2],
            robot[3]
          )
        }
        iteration++
      }
    }

    fun findLowestSafetyFactor() {
      val robots = getRobots() ?: return
      // Find the lowest safety factor, because safety factor is lowest when robots are grouped up, which they will be for the tree
      var minSafety = Pair(Integer.MAX_VALUE, -1)
      var safestRobots = listOf<Pair<Int, Int>>()
      // Go up to max iterations before they start another loop
      for (i in 0..<WIDTH * HEIGHT) {
        val movedRobots = moveRobots(robots, i)
        val safetyFactor = calcSafetyFactor(movedRobots)
        if (safetyFactor < minSafety.first) {
          minSafety = Pair(safetyFactor, i)
          safestRobots = movedRobots
        }
      }
      println("Result is: %s".format(minSafety.second))
      val robotsSet = safestRobots.toHashSet()
      for (y in 0..<HEIGHT) {
        for (x in 0..<WIDTH) {
          if (robotsSet.contains(Pair(x, y))) {
            print("#")
          } else {
            print(".")
          }
        }
        println()
      }
    }
  }
}

fun main() {
  Day14.findLowestSafetyFactor()
}