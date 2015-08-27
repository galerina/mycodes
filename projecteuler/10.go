// Summation of primes
// Problem 10
// The sum of the primes below 10 is 2 + 3 + 5 + 7 = 17.

// Find the sum of all the primes below two million.

package main

import (
	"fmt"
	"misc"
)

func main() {
	sum := 0
	limit := 2000000

	getNextPrime := misc.GetPrimesIterator()

	for prime := getNextPrime(); prime < limit; prime = getNextPrime() {
		sum += prime
	}

	fmt.Printf("Sum of primes below %v is %v\n", limit, sum)
}
