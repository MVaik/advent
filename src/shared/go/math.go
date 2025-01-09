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
