package main

import (
	"fmt"
	"strings"

	utils "github.com/MVaik/advent/src/shared/go"
)

func main() {
	lines := utils.ReadLinesWithSeparator("../../inputs/2025/day2.txt", ",")
	partOneSum := 0
	partTwoSum := 0
	for _, line := range lines {
		rangeSplit := utils.MatchPositiveIntsAndParse(line)
		start := rangeSplit[0]
		end := rangeSplit[1]
		for i := start; i <= end; i++ {
			stringifiedValue := fmt.Sprint(i)
			length := len(stringifiedValue)
			// Part one
			midpoint := length / 2
			if stringifiedValue[0:midpoint] == stringifiedValue[midpoint:length] {
				partOneSum += i
			}

			// Part two
			for j := 1; j < length; j++ {
				subString := stringifiedValue[0:j]
				if stringifiedValue == strings.Repeat(subString, length/j) {
					partTwoSum += i
					break
				}
			}
		}
	}

	println("Part one result: ", partOneSum)
	println("Part two result: ", partTwoSum)
}
