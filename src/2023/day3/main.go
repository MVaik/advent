package main

import (
	"strconv"
	"unicode"

	utils "github.com/MVaik/advent/src/shared/go"
)

func main() {
	input := utils.ReadLines("../../inputs/2023/day3.txt")
	visitedPositions := utils.NewSet[utils.Position]()
	sum := 0
	gears := make(map[utils.Position][]int)

	for y, line := range input {
		for x, char := range line {
			charPos := utils.NewPosition(x, y).WithGrid(&input)
			// Skip already processed numbers
			if visitedPositions.Contains(charPos) {
				continue
			}
			if unicode.IsDigit(char) {
				numString := string(char)
				i := x + 1
				lineLength := len(line)
				// Store the position that potentially makes our number valid
				// Numbers don't have multiple symbols making them valid
				validityPos := utils.NewPosition(-100, -100)
				for _, neighbor := range charPos.AllGridNeighbors() {
					neighborChar := rune(input[neighbor.Y][neighbor.X])
					if !unicode.IsDigit(neighborChar) && neighborChar != '.' {
						validityPos = neighbor
					}
				}
				// Keep going right as long as we're in bounds and the chars are digits
				for i < lineLength && unicode.IsDigit(rune(line[i])) {
					numString += string(line[i])
					connectedPos := utils.NewPosition(i, y).WithGrid(&input)
					visitedPositions.Add(connectedPos)
					if validityPos.X == -100 {
						for _, neighbor := range connectedPos.AllGridNeighbors() {
							neighborChar := rune(input[neighbor.Y][neighbor.X])
							if !unicode.IsDigit(neighborChar) && neighborChar != '.' {
								validityPos = neighbor
							}
						}
					}
					i++
				}

				if validityPos.X != -100 {
					if num, err := strconv.Atoi(numString); err == nil {
						sum += num
						// Keep track of numbers connected to asterisk aka gears
						if input[validityPos.Y][validityPos.X] == '*' {
							connectedGears, ok := gears[validityPos]
							if !ok {
								newGears := make([]int, 1)
								newGears = append(newGears, num)
								gears[validityPos] = newGears
							}
							gears[validityPos] = append(connectedGears, num)
						}
					}

				}

			}
		}
	}

	gearRatiosSum := 0
	for _, v := range gears {
		// We only care about gears that form one pair
		if len(v) == 2 {
			gearRatio := 1
			for _, num := range v {
				gearRatio *= num
			}
			gearRatiosSum += gearRatio
		}
	}

	println("Part 1 sum: ", sum)
	println("Part 2 sum: ", gearRatiosSum)
}
