package utils

type Position struct {
	x    int
	y    int
	grid *[]string
}

type Direction struct {
	x int
	y int
}

var AllDirections = [8]Direction{
	{x: -1, y: -1},
	{x: 0, y: -1},
	{x: 1, y: -1},
	{x: -1, y: 0},
	{x: 1, y: 0},
	{x: -1, y: 1},
	{x: 0, y: 1},
	{x: 1, y: 1},
}

func NewPosition(x int, y int) Position {
	return Position{x: x, y: y}
}

func (p *Position) AddDirection(direction Direction) Position {
	return Position{x: p.x + direction.x, y: p.y + direction.y}
}

func (p *Position) ValidDirection(direction Direction) bool {
	if p.grid == nil {
		return false
	}

	newX := p.x + direction.x
	newY := p.y + direction.y

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
