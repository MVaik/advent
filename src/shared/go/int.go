package utils

import (
	"fmt"
	"strconv"
	"strings"
)

// Stringify all ints in array
func StringifyInts(ints []int) []string {
	stringifiedNums := make([]string, 0, len(ints))

	for _, int := range ints {
		stringifiedNums = append(stringifiedNums, fmt.Sprint(int))
	}
	return stringifiedNums
}

// Join input array into one string and parse that into a new number
func JoinInts(ints []int) int {
	num, err := strconv.Atoi(strings.Join(StringifyInts(ints), ""))
	if err != nil {
		fmt.Printf("Failed to join ints %v, err: %v", ints, err)
	}
	return num
}
