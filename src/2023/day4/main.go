package main

import (
	"math"
	"regexp"
	"strings"

	utils "github.com/MVaik/advent/src/shared/go"
)

func main() {
	input := utils.ReadLines("../../inputs/2023/day4.txt")
	partOneSum := 0

	numberRegex := regexp.MustCompile(`\d+`)
	cards := make(map[int]int)
	for i, line := range input {
		splitAtColon := strings.Split(line, ": ")
		numbers := strings.Split(splitAtColon[1], " | ")
		winningNumbers := numberRegex.FindAllString(numbers[0], -1)
		potentialNumbers := numberRegex.FindAllString(numbers[1], -1)
		// Map works too but this is simpler
		winningNumbersSet := utils.NewSet[string]()
		matches := 0
		for _, num := range winningNumbers {
			winningNumbersSet.Add(num)
		}

		// Find all matching numbers
		for _, num := range potentialNumbers {
			if winningNumbersSet.Contains(num) {
				matches++
			}
		}

		// Remember matches count for each card
		cards[i+1] = matches
		// Points double for each one after first, so we add 2 ^ (matches - 1)
		partOneSum += int(math.Pow(float64(2), float64(matches-1)))
	}

	queue := make([]int, 0)

	// Use all the original cards to start off the queue
	for key := range cards {
		queue = append(queue, key)
	}

	partTwoSum := 0
	for len(queue) > 0 {
		// Get first element and slice array to exclude it
		curr := queue[0]
		queue = queue[1:]

		matches := cards[curr]
		// Add new card copies to the queue
		for i := 1; i <= matches; i++ {
			queue = append(queue, curr+i)
		}
		partTwoSum++
	}

	println("Part one sum: ", partOneSum)
	println("Part two sum: ", partTwoSum)
}
