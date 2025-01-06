package main

import (
	"strings"

	utils "github.com/MVaik/advent/src/shared/go"
)

func findBetterTimesAndMultTarget(times []int, distances []int, target *int) {
	for i, time := range times {
		distanceToBeat := distances[i]
		betterTimes := 0
		for i := time - 1; i > 0; i-- {
			// Millimeters traveled is iteration aka time held * how much time we have left
			time := i * (time - i)
			if time > distanceToBeat {
				betterTimes++
			}
		}
		*target *= betterTimes
	}
}

func main() {
	input := utils.ReadString("../../inputs/2023/day6.txt")
	splitInput := strings.Split(input, "\r\n")
	times := utils.MatchIntsAndParse(splitInput[0])
	distances := utils.MatchIntsAndParse(splitInput[1])
	partOneSum := 1
	findBetterTimesAndMultTarget(times, distances, &partOneSum)

	joinedTimes := utils.JoinInts(times)
	joinedDistances := utils.JoinInts(distances)
	partTwoSum := 1
	findBetterTimesAndMultTarget([]int{joinedTimes}, []int{joinedDistances}, &partTwoSum)

	println("Part one sum:", partOneSum)
	println("Part two sum:", partTwoSum)

}
