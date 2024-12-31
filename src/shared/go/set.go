package utils

import (
	"fmt"
	"strings"
)

type Set[T comparable] struct {
	elements map[T]struct{}
}

func NewSet[T comparable]() *Set[T] {
	return &Set[T]{
		elements: make(map[T]struct{}),
	}
}

// Insert a value into the set
func (s *Set[T]) Add(value T) {
	s.elements[value] = struct{}{}
}

// Delete a value from set
func (s *Set[T]) Remove(value T) {
	delete(s.elements, value)
}

// Check if value exists in set
func (s *Set[T]) Contains(value T) bool {
	_, found := s.elements[value]
	return found
}

// Get number of values in set
func (s *Set[T]) Size() int {
	return len(s.elements)
}

// Get set values as an array
func (s *Set[T]) List() []T {
	keys := make([]T, 0, len(s.elements))
	for key := range s.elements {
		keys = append(keys, key)
	}
	return keys
}

func (s *Set[T]) String() string {
	var output strings.Builder
	for k := range s.elements {
		if output.Len() > 0 {
			output.WriteString(", ")
		}
		output.WriteString(fmt.Sprint(k))
	}
	return output.String()
}
