package utils

import (
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
