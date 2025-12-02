package utils

func FindGCD(a, b int) int {
	for b != 0 {
		t := b
		b = a % b
		a = t
	}
	return a
}

func FindLCM(a, b int) int {
	result := a * b / FindGCD(a, b)

	return result
}

func FindLCMForArray(ints []int) int {
	result := 1
	for i := range ints {
		result = FindLCM(result, ints[i])
	}
	return result
}

func PositiveMod(a, b int) int {
	a = a % b
	if a >= 0 {
		return a
	}
	if b < 0 {
		return a - b
	}
	return a + b
}

func IntegerAbs(val int) int {
	if val < 0 {
		return -val
	}
	return val
}
