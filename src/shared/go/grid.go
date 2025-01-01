package utils

import (
	"fmt"
	"strconv"
	"strings"
)

type Position struct {
	X    int `json:"X"`
	Y    int `json:"Y"`
	grid *[]string
}

func (p *Position) UnmarshalText(text []byte) error {
	stringifiedText := strings.ToLower(string(text))
	coords := strings.Split(stringifiedText, ", ")
	// Remove unnecessary data
	replacer := strings.NewReplacer("{", "", "}", "", "x: ", "", "y: ", "")
	unparsedX := replacer.Replace(coords[0])
	unparsedY := replacer.Replace(coords[1])
	newPos := Position{}
	if num, err := strconv.Atoi(unparsedX); err == nil {
		newPos.X = num
	}

	if num, err := strconv.Atoi(unparsedY); err == nil {
		newPos.Y = num
	}

	*p = newPos

	return nil
}

func (p Position) MarshalText() ([]byte, error) {
	stringifiedPosition := fmt.Sprintf("{x: %v, y: %v}", p.X, p.Y)

	return []byte(stringifiedPosition), nil
}

type Direction struct {
	X int
	Y int
}

var AllDirections = [8]Direction{
	{X: -1, Y: -1},
	{X: 0, Y: -1},
	{X: 1, Y: -1},
	{X: -1, Y: 0},
	{X: 1, Y: 0},
	{X: -1, Y: 1},
	{X: 0, Y: 1},
	{X: 1, Y: 1},
}

func NewPosition(x int, y int) Position {
	return Position{X: x, Y: y}
}

func (p *Position) AddDirection(direction Direction) Position {
	return Position{X: p.X + direction.X, Y: p.Y + direction.Y}
}

func (p *Position) ValidDirection(direction Direction) bool {
	if p.grid == nil {
		return false
	}

	newX := p.X + direction.X
	newY := p.Y + direction.Y

	return newY >= 0 && newY < len(*p.grid) && newX >= 0 && newX < len((*p.grid)[0])
}

func (p Position) WithGrid(grid *[]string) Position {
	p.grid = grid
	return p
}

func (p *Position) AllGridNeighbors() []Position {
	if p.grid == nil {
		var neighbors []Position
		return neighbors
	}
	// Count how much space we need
	validNeighbors := 0
	for _, direction := range AllDirections {
		if p.ValidDirection(direction) {
			validNeighbors++
		}
	}

	// Reserve the space needed and fill it
	neighbors := make([]Position, validNeighbors)

	for _, direction := range AllDirections {
		if p.ValidDirection(direction) {
			neighbors = append(neighbors, p.AddDirection(direction))
		}
	}
	return neighbors
}
