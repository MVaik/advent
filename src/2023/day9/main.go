package main

import (
	"math"

	utils "github.com/MVaik/advent/src/shared/go"
)

func main() {
	input := utils.ReadLines("../../inputs/2023/day9.txt")

	partOneSum := 0
	partTwoSum := 0

	for _, line := range input {
		lineNumbers := utils.MatchIntsAndParse(line)
		queue := make([][]int, 1)
		queue[0] = lineNumbers
		for {
			newLineNumbers := make([]int, 0)
			diff := math.MinInt
			// Create a new array out of diffs
			for i := 1; i < len(lineNumbers); i++ {
				diff = lineNumbers[i] - lineNumbers[i-1]
				newLineNumbers = append(newLineNumbers, diff)
			}
			// Easier to go to 1 than sum stuff up etc, input can have weird edge cases
			// Break if diffs are no longer changing
			if len(newLineNumbers) == 1 || diff == math.MinInt {
				break
			}
			queue = append(queue, newLineNumbers)
			lineNumbers = newLineNumbers
		}

		lineStartDiff := 0
		lineEndDiff := 0
		queueStart := len(queue) - 2

		// Go backwards from queued up diffs to get new base value
		for i := queueStart; i >= 0; i-- {
			prev := queue[i+1]
			prevLast := prev[len(prev)-1]
			curr := queue[i]
			currLast := curr[len(curr)-1]
			if i != queueStart {
				lineEndDiff = currLast + lineEndDiff
				lineStartDiff = curr[0] - lineStartDiff
			} else {
				lineEndDiff = prevLast + currLast
				lineStartDiff = curr[0] - prev[0]
			}
		}
		partOneSum += lineEndDiff
		partTwoSum += lineStartDiff
	}

	println("Part one sum:", partOneSum)
	println("Part two sum:", partTwoSum)
}
