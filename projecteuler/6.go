// Sum square difference
// Problem 6
// The sum of the squares of the first ten natural numbers is,

// 1**2 + 2**2 + ... + 10**2 = 385
// The square of the sum of the first ten natural numbers is,

// (1 + 2 + ... + 10)**2 = 552 = 3025
// Hence the difference between the sum of the squares of the first ten natural numbers and the square of the sum is 3025 − 385 = 2640.

// Find the difference between the sum of the squares of the first one hundred natural numbers and the square of the sum.Sum square difference
package main

import "fmt"

func main() {
	loLimit := 1
	hiLimit := 100

	// Compute sum of the squares and sum
	sumOfSquares := 0
	sum := 0
	for i := loLimit; i <= hiLimit; i++ {
		sumOfSquares += i*i
		sum += i
	}

	fmt.Printf("Difference: %v - %v = %v\n", sum*sum, sumOfSquares,  sum*sum - sumOfSquares)
}
