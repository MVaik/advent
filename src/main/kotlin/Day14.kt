import shared.Utils
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

    private fun wrapCoord(input: Int, max: Int): Int {
      // Handle negative values
      return ((input % max) + max) % max
    }

    fun calcSafetyFactor() {
      val input = Utils.readLines("/inputs/day14.txt") ?: return
      val iterationCount = 100
      val robots = input.map { robot ->
        // Get all digits in the row
        val matches = robotCoordsRegex.findAll(robot)
        val values = ArrayList<Int>()
        for (match in matches) {
          values.add(match.value.toInt())
        }
        values
      }
      val movedRobots = robots.map { robot ->
        val (x, y, xVel, yVel) = robot
        Pair(
          wrapCoord((x + xVel * iterationCount), WIDTH),
          wrapCoord((y + yVel * iterationCount), HEIGHT),
        )
      }
      val ignoredX = WIDTH / 2
      val ignoredY = HEIGHT / 2
      // Filter out ignored coords
      val safetyFactor =
        movedRobots.filter { robot -> robot.first != ignoredX && robot.second != ignoredY }.groupBy { robot ->
          // Group into 4 quadrants based on whether they're below ignore coords
          Pair(robot.first < ignoredX, robot.second < ignoredY)
          // Multiply each robot count together
        }.values.fold(1) { acc, groupedRobots ->
          acc * groupedRobots.size
        }
      println("Result: %s".format(safetyFactor))
    }

    fun drawRobotsToImage() {
      val input = Utils.readLines("/inputs/day14.txt") ?: return
      var robots = input.map { robot ->
        // Get all digits in the row
        val matches = robotCoordsRegex.findAll(robot)
        val values = ArrayList<Int>()
        for (match in matches) {
          values.add(match.value.toInt())
        }
        values
      }
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
  }
}

fun main() {
  Day14.drawRobotsToImage()
}