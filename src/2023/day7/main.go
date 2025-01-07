package main

import (
	"sort"
	"strings"

	utils "github.com/MVaik/advent/src/shared/go"
)

func getRank(cards map[rune]int, highestNonJoker *rune) int {
	rank := -1
	highest := -1
	secondHighest := -1
	for char, v := range cards {
		// Keep track of the highest non joker card so we can assign joker to that
		if char != 74 && (int(*highestNonJoker) == -1 || v > cards[*highestNonJoker]) {
			*highestNonJoker = char
		}
		// 5 or 4 of a kind are in separate ranks
		if v == 5 {
			highest = v
			rank = 7
		} else if v == 4 {
			secondHighest = v
			rank = 6
		}
		if rank < 6 {
			if highest == -1 || v > highest {
				secondHighest = highest
				highest = v
			} else if v > secondHighest {
				secondHighest = v
			}
		}
	}

	if rank == -1 {
		if highest == 3 {
			// Differentiate full house from three of a kind
			if secondHighest == 2 {
				rank = 5
			} else {
				rank = 4
			}
		} else {
			// Rank doesn't strictly matter after this point, just has to be lower than the previous rules
			rank = 6 - len(cards)
		}
	}
	return rank
}

func main() {
	input := utils.ReadLines("../../inputs/2023/day7.txt")
	hands := make([][]int, 0, len(input))

	cardStrength := map[string]int{
		"A": 13, "K": 12, "Q": 11, "J": 10, "T": 9, "9": 8, "8": 7, "7": 6, "6": 5, "5": 4, "4": 3, "3": 2, "2": 1,
	}

	for _, row := range input {
		hand := strings.Split(row, " ")

		handCards := hand[0]
		handBet := utils.MatchIntAndParse(hand[1])

		cardsMap := make(map[rune]int, 5)

		for _, char := range handCards {
			_, ok := cardsMap[char]
			if ok {
				cardsMap[char]++
			} else {
				cardsMap[char] = 1
			}
		}
		highestNonJoker := rune(-1)
		regularRank := getRank(cardsMap, &highestNonJoker)
		jokerRune := []rune("J")[0]

		jokerCardsMap := make(map[rune]int, 5)
		// Get new counts with joker filling the most common non-joker card instead
		for _, char := range handCards {
			if char == jokerRune {
				char = highestNonJoker
			}
			_, ok := jokerCardsMap[char]
			if ok {
				jokerCardsMap[char]++
			} else {
				jokerCardsMap[char] = 1
			}
		}

		highestNonJoker = rune(-1)

		jokerRank := getRank(jokerCardsMap, &highestNonJoker)

		handInfo := make([]int, 0, 8)
		// Track both ranks at the same time
		handInfo = append(handInfo, regularRank, jokerRank, handBet)
		// Add the rest of the cards as their strengths
		for _, card := range handCards {
			handInfo = append(handInfo, cardStrength[string(card)])
		}
		hands = append(hands, handInfo)
	}

	sort.SliceStable(hands, func(i, j int) bool {
		firstStrength := hands[i][0]
		secondStrength := hands[j][0]
		if firstStrength == secondStrength {
			for cardIndex := 3; cardIndex < 8; cardIndex++ {
				firstStrength = hands[i][cardIndex]
				secondStrength = hands[j][cardIndex]
				if firstStrength != secondStrength {
					break
				}
			}
		}
		return firstStrength < secondStrength
	})

	partOneSum := 0

	for i, hand := range hands {
		partOneSum += (i + 1) * hand[2]
	}

	// Re-sort with joker rules for part 2
	sort.SliceStable(hands, func(i, j int) bool {
		firstStrength := hands[i][1]
		secondStrength := hands[j][1]
		if firstStrength == secondStrength {
			for cardIndex := 3; cardIndex < 8; cardIndex++ {
				firstStrength = hands[i][cardIndex]
				secondStrength = hands[j][cardIndex]
				if firstStrength == cardStrength["J"] {
					firstStrength = 0
				}
				if secondStrength == cardStrength["J"] {
					secondStrength = 0
				}
				if firstStrength != secondStrength {
					break
				}
			}
		}
		return firstStrength < secondStrength
	})

	partTwoSum := 0
	for i, hand := range hands {
		partTwoSum += (i + 1) * hand[2]
	}

	println("Part one sum:", partOneSum)
	println("Part two sum:", partTwoSum)
}
