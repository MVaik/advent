package utils

import (
	"fmt"
	"os"
	"strings"
)

func ReadString(input string) string {
	b, err := os.ReadFile(input)
	if err != nil {
		fmt.Print(err)
	}

	return string(b)
}

func ReadLines(input string) []string {
	b, err := os.ReadFile(input)
	if err != nil {
		fmt.Print(err)
	}

	return strings.Split(string(b), "\n")
}
