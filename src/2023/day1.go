package main

import (
	"strconv"
	"strings"
	"unicode"

	utils "github.com/MVaik/advent/src/shared/go"
)

func main() {
	lines := utils.ReadLines("../inputs/2023/day1.txt")
	sum := 0
	for _, line := range lines {
		firstDigitIndex := strings.IndexFunc(line, unicode.IsDigit)
		lastDigitIndex := strings.LastIndexFunc(line, unicode.IsDigit)
		numString := ""
		if firstDigitIndex != -1 {
			numString += string(line[firstDigitIndex])
		}
		if lastDigitIndex != -1 {
			numString += string(line[lastDigitIndex])
		}
		num, err := strconv.Atoi(numString)
		if err == nil {
			sum += num
		}
	}

	println("Part 1 sum: ", sum)
}
