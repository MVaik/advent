package main

import (
	"strconv"

	utils "github.com/MVaik/advent/src/shared/go"
)

var digitMap = map[string]string{
	"1":     "1",
	"2":     "2",
	"3":     "3",
	"4":     "4",
	"5":     "5",
	"6":     "6",
	"7":     "7",
	"8":     "8",
	"9":     "9",
	"one":   "1",
	"two":   "2",
	"three": "3",
	"four":  "4",
	"five":  "5",
	"six":   "6",
	"seven": "7",
	"eight": "8",
	"nine":  "9",
}

func main() {
	lines := utils.ReadLines("../../inputs/2023/day1.txt", "")
	partOneSum := 0
	partTwoSum := 0
	for _, line := range lines {
		firstDigitIndex := -1
		lastDigitIndex := -1
		firstDigitRange := []int{-1, -1}
		lastDigitRange := []int{-1, -1}

		for i := range line {
			j := i + 1
			// Get slices of string and attempt matches via digits map
			for j <= len(line) {
				if _, ok := digitMap[string(line[i:j])]; ok {
					if firstDigitRange[0] == -1 {
						firstDigitRange[0] = i
						firstDigitRange[1] = j
					}
					// Override existing range if a new one matches
					if lastDigitRange[0] < i {
						lastDigitRange[0] = i
						lastDigitRange[1] = j
					}

					// Keep track of one digit indices for part 1
					if j-1 == i {
						if firstDigitIndex == -1 {
							firstDigitIndex = i
						}

						if lastDigitIndex < i {
							lastDigitIndex = i
						}
					}
				}
				j++
			}
		}
		partOneNumString := ""
		if firstDigitIndex != -1 {
			partOneNumString += string(line[firstDigitIndex])
		}
		if lastDigitIndex != -1 {
			partOneNumString += string(line[lastDigitIndex])
		}

		if num, err := strconv.Atoi(partOneNumString); err == nil {
			partOneSum += num
		}

		partTwoNumString := ""
		if numString, ok := digitMap[string(line[firstDigitRange[0]:firstDigitRange[1]])]; ok {
			partTwoNumString += numString
		}

		if numString, ok := digitMap[string(line[lastDigitRange[0]:lastDigitRange[1]])]; ok {
			partTwoNumString += numString
		}

		if num, err := strconv.Atoi(partTwoNumString); err == nil {
			partTwoSum += num
		}
	}

	println("Part 1 sum: ", partOneSum)

	println("Part 2 sum: ", partTwoSum)
}
