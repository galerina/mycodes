package misc

import (
	// "fmt"

	"math/big"
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

func DigitSum(n *big.Int) uint32 {
	str := n.String()
	var sum uint32 = 0
	for i := range str {
		sum += uint32(str[i] - '0')
	}

	return sum
}


// Return an array containing the proper divisors for each number less than n
// Idea: could unite this and the "prime" functions
func Divisors(n int) [][]int {
	divisors := [][]int{}
	
	for i := 0; i < n; i++ {
		divisors = append(divisors, []int{})
	}

	for i := 1; i < n; i++ {
		multiple := i

		for ; multiple < n; multiple = multiple + i {
			if i < multiple {
				divisors[multiple] = append(divisors[multiple],i)
			}
		}
	}

	return divisors
}
