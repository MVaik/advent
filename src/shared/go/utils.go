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

func ReadLines(input string, separator string) []string {
	if len(separator) > 0 {

		return strings.Split(ReadString(input), separator)
	} else {
		return strings.Split(ReadString(input), "\r\n")
	}
}
