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
			destinationStart := parsedInts[j]
			sourceStart := parsedInts[j+1]
			rangeLength := parsedInts[j+2]
			// Add ranges as start, end instead of start, length
			parsedRange = append(parsedRange, []int{sourceStart, sourceStart + rangeLength - 1, destinationStart, destinationStart + rangeLength - 1})
		}
		parsedSections = append(parsedSections, parsedRange)
	}

	queue := make([][]int, 0, len(seeds))

	for _, seed := range seeds {
		queue = append(queue, []int{1, 0, seed, seed})
	}

	for i := 0; i < len(seeds); i += 2 {
		// Add seeds as start, end instead of start, length
		queue = append(queue, []int{2, 0, seeds[i], seeds[i] + seeds[i+1] - 1})
	}

	partOneLowest := -1
	partTwoLowest := -1

	for len(queue) > 0 {
		curr := queue[0]
		queue = queue[1:]
		part := curr[0]
		level := curr[1]
		currStart := curr[2]
		currEnd := curr[3]

		if level == 7 {
			if part == 1 && (partOneLowest == -1 || partOneLowest > currStart) {
				partOneLowest = currStart
			}
			if part == 2 && (partTwoLowest == -1 || partTwoLowest > currStart) {
				partTwoLowest = currStart
			}
			continue
		}
		rangeFound := false
		for _, potentialRange := range parsedSections[level] {
			sourceStart := potentialRange[0]
			sourceEnd := potentialRange[1]
			destinationStart := potentialRange[2]
			destinationEnd := potentialRange[3]

			// Do the ranges overlap?
			if currEnd >= sourceStart && sourceEnd >= currStart {
				// Add the regular piece that is contained in source range, accounting for any diffs in start or end
				queue = append(queue, []int{part, level + 1, destinationStart + max(currStart-sourceStart, 0), destinationEnd - max(sourceEnd-currEnd, 0)})

				if currStart < sourceStart {
					// Send unmatched piece back to checking the same level
					queue = append(queue, []int{part, level, currStart, sourceStart - 1})
				} else if currEnd > sourceEnd {
					queue = append(queue, []int{part, level, sourceEnd + 1, currEnd})
				}
				rangeFound = true
			}
		}
		// Keep the unchanged range if nothing matched
		if !rangeFound {
			queue = append(queue, []int{part, level + 1, currStart, currEnd})
		}
	}
	println("Part one result: ", partOneLowest)
	println("Part two result: ", partTwoLowest)
}
