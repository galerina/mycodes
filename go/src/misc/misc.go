package misc

import (
	// "fmt"
)

func IsMultiple(n, base int) bool {
	return (n%base == 0)
}

func FibList(n int) []int {
	prev := 1
	current := 1
	list := make([]int, 0, n/2)
	for ;current <= n; prev, current = current, current+prev {
		list = append(list, current)
	}

	return list
}

func Max(a,b int) int {
	if a > b {
		return a
	} else {
		return b
	}
}

func Product(numbers []int) int {
	product := 1
	for _, n := range numbers {
		product *= n
	}

	return product
}

func CountDivisors(n int) int {
	if n <= 1 {
		return 1
	}

	divisorCount := 2

	maxDivisor := n

	for i := 2; i < maxDivisor; i++ {
		if IsMultiple(n, i) {
			divisorCount++
			maxDivisor = n / i
			if maxDivisor != i {
				divisorCount++
			}
		}
	}

	return divisorCount
}

func GetTriangleNumberIterator() (func() int) {
	i := 1
	next := 1

	return func() int {
		current := next
		i++
		next = next + i
		return current
	}

}
