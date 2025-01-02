package utils

type Pair[FirstType any, SecondType any] struct {
	First  FirstType
	Second SecondType
}

func NewPair[FirstType any, SecondType any](first FirstType, second SecondType) *Pair[FirstType, SecondType] {
	return &Pair[FirstType, SecondType]{
		First: first, Second: second,
	}
}
