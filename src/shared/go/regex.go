package utils

import (
	"fmt"
	"regexp"
	"strconv"
)

var NUMBER_REGEX = regexp.MustCompile(`\d+`)

func MatchIntsAndParse(input string) []int {
	matchedNums := NUMBER_REGEX.FindAllString(input, -1)
	parsedInts := make([]int, 0, len(matchedNums))

	for _, num := range matchedNums {
		parsedInt, _ := strconv.Atoi(num)
		parsedInts = append(parsedInts, parsedInt)
	}
	return parsedInts
}

func MatchIntAndParse(input string) int {
	matchedNum := NUMBER_REGEX.FindString(input)
	parsedInt, err := strconv.Atoi(matchedNum)
	if err != nil {
		fmt.Printf("Failed to match and parse int from %v, err: %v", input, err)
		return 0
	}
	return parsedInt
}
