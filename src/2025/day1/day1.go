package main

import (
	utils "github.com/MVaik/advent/src/shared/go"
)

func main() {
	lines := utils.ReadLines("../../inputs/2025/day1.txt")
	partOneZeroCount := 0
	partTwoZeroCount := 0
	val := 50
	for _, line := range lines {
		clicks := utils.MatchIntAndParse(line[1:])
		// All the remainder etc math stuff is kinda problematic, simpler to just loop through for this one
		for i := clicks; i > 0; i-- {
			if line[0] == 'R' {
				val++
			} else {
				val--
			}
			val = utils.PositiveMod(val, 100)
			if val == 0 {
				partTwoZeroCount++
			}
		}
		if val == 0 {
			partOneZeroCount++
		}
	}

	println("Part one result: ", partOneZeroCount)
	println("Part two result: ", partTwoZeroCount)
}
