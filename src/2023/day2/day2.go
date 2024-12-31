package main

import (
	"strconv"
	"strings"

	utils "github.com/MVaik/advent/src/shared/go"
)

func main() {
	lines := utils.ReadLines("../../inputs/2023/day2.txt", "")

	partOneMaxCubes := map[string]int{
		"red":   12,
		"green": 13,
		"blue":  14,
	}

	idsSum := 0
	minimumCubesPowerSum := 0

	for id, line := range lines {
		splitGameString := strings.Split(line, ": ")
		sets := strings.Split(splitGameString[1], "; ")
		// Keep track of the max amount of cubes needed per game
		currentGameCubes := map[string]int{
			"red":   0,
			"blue":  0,
			"green": 0,
		}
		isValidGame := true
		for _, set := range sets {
			currentSetCubes := map[string]int{
				"red":   0,
				"blue":  0,
				"green": 0,
			}
			for _, cube := range strings.Split(set, ", ") {
				cubeSplit := strings.Split(cube, " ")
				count := cubeSplit[0]
				cubeType := cubeSplit[1]
				if maxCubes, ok := partOneMaxCubes[cubeType]; ok {
					if num, err := strconv.Atoi(count); err == nil {
						currentSetCubes[cubeType] += num
						// Update the cube max for the game
						currentGameCubes[cubeType] = max(currentGameCubes[cubeType], num)
						if currentSetCubes[cubeType] > maxCubes {
							// Exclude this game from counting in ids sum
							isValidGame = false
						}
					}
				}
			}

		}
		if isValidGame {
			idsSum += id + 1
		}

		gamePower := 1
		// Multiply each cube count together to get their power
		for _, count := range currentGameCubes {
			gamePower *= count
		}

		minimumCubesPowerSum += gamePower
	}

	println("Part one sum: ", idsSum)

	println("Part two sum: ", minimumCubesPowerSum)
}
