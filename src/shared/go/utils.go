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

func ReadLinesWithSeparator(input string, separator string) []string {
	return strings.Split(ReadString(input), separator)
}

// Default separator \r\n used, use ReadLinesWithSeparator for other separators
func ReadLines(input string) []string {
	return strings.Split(ReadString(input), "\r\n")
}
