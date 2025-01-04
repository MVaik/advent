package main

import (
	"strings"

	utils "github.com/MVaik/advent/src/shared/go"
)

func main() {
	input := utils.ReadString("../../inputs/2023/day5.txt")
	sections := strings.Split(input, "\r\n\r\n")
	seeds := utils.MatchIntsAndParse(sections[0])
	parsedSections := make([][][]int, 0)
	for i := 1; i < len(sections); i++ {
		parsedInts := utils.MatchIntsAndParse(sections[i])
		parsedRange := make([][]int, 0, len(parsedInts)/3)
		for j := 0; j < len(parsedInts)-2; j += 3 {
			parsedRange = append(parsedRange, parsedInts[j:j+3])
		}
		parsedSections = append(parsedSections, parsedRange)
	}

	queue := make([][]int, 0, len(seeds))

	for _, seed := range seeds {
		queue = append(queue, []int{0, seed, 1})
	}

	partOneLowest := -1

	for len(queue) > 0 {
		curr := queue[0]
		queue = queue[1:]

		if curr[0] == 7 {
			if partOneLowest == -1 || partOneLowest > curr[1] {
				partOneLowest = curr[1]
			}
			continue
		}
		rangeFound := false
		for _, potentialRange := range parsedSections[curr[0]] {
			// If the current number fits between our range, add the transformation to the queue
			if curr[1] >= potentialRange[1] && curr[1] <= potentialRange[1]+potentialRange[2] {
				newVal := potentialRange[0] + curr[1] - potentialRange[1]
				queue = append(queue, []int{curr[0] + 1, newVal, curr[2]})
				rangeFound = true
			}
		}
		// Add the number itself back into the queue for next transformation if it didn't have any matching ranges
		if !rangeFound {
			queue = append(queue, []int{curr[0] + 1, curr[1], curr[2]})
		}
	}
	println("Part one result: ", partOneLowest)
}
