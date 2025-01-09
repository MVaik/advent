package main

import (
	"regexp"

	utils "github.com/MVaik/advent/src/shared/go"
)

func main() {
	input := utils.ReadLinesWithSeparator("../../inputs/2023/day8.txt", "\r\n\r\n")
	steps := input[0]
	nodeRegex := regexp.MustCompile(`[\dA-Z]+`)
	nodes := nodeRegex.FindAllString(input[1], -1)
	mappedSteps := make([]int, len(steps))
	mappedNodes := make(map[string][]string)
	stepRunes := []rune("LR")
	// Simpler to compare actual ints rather than rune type
	for i, step := range steps {
		if step == stepRunes[0] {
			mappedSteps[i] = 0
		} else {
			mappedSteps[i] = 1
		}
	}

	nodesEndingInA := make([]string, 0)
	for i := 0; i < len(nodes); i += 3 {
		mappedNodes[nodes[i]] = []string{
			nodes[i+1],
			nodes[i+2],
		}
		// Store the part 2 nodes while mapping others
		if string(nodes[i][2]) == "A" {
			nodesEndingInA = append(nodesEndingInA, nodes[i])
		}
	}

	partOneCount := 0
	curr := "AAA"
	// Basic AAA to ZZZ for part 1
	for curr != "ZZZ" {
		for _, step := range mappedSteps {
			curr = mappedNodes[curr][step]
			partOneCount++
		}
	}

	stepTwoNodesCount := len(nodesEndingInA)
	distanceToZ := make([]int, stepTwoNodesCount)
	doneNodes := make([]int, stepTwoNodesCount)

	// Find a-z steps for each node
	for {
		completeCount := 0
		for i := range nodesEndingInA {
			if doneNodes[i] == 1 {
				completeCount++
				continue
			}
			for _, step := range mappedSteps {
				nodesEndingInA[i] = mappedNodes[nodesEndingInA[i]][step]
				distanceToZ[i]++
			}
			// Found the steps needed for this node
			if string(nodesEndingInA[i][2]) == "Z" {
				doneNodes[i] = 1
			}
		}
		if stepTwoNodesCount == completeCount {
			break
		}
	}
	println("Part one count:", partOneCount)
	// Get lowest common multiple of all the distances
	println("Part two count:", utils.FindLCMForArray(distanceToZ))
}
