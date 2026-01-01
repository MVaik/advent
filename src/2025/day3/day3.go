package main

import (
	utils "github.com/MVaik/advent/src/shared/go"
)

func main() {
	lines := utils.ReadLines("../../inputs/2025/day3.txt")

	partOneSum := 0
	partTwoSum := 0
	for _, line := range lines {
		partOneSum += findMaxSum(line, 2)
		partTwoSum += findMaxSum(line, 12)
	}
	println("Part one result: ", partOneSum)
	println("Part two result: ", partTwoSum)
}

func findMaxSum(line string, maxLength int) int {
	length := len(line)
	start := 0
	sum := 0

	for i := range maxLength {
		max := 0
		for j := start; j < length-maxLength+i+1; j++ {
			num := utils.ParseIntFromByte(line[j])
			if num > max {
				max = num
				start = j + 1
			}
		}
		sum = sum*10 + max
	}
	return sum
}
