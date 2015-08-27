// Largest prime factor
// Problem 3
// The prime factors of 13195 are 5, 7, 13 and 29.

// What is the largest prime factor of the number 600851475143 ?

package main

import (
	"misc"
	"fmt"
)

func findLargestPrimeFactor(n int) int {
	nextPrime := misc.GetPrimesIterator(n)
	for prime := nextPrime(); prime > 0; prime = nextPrime() {
		if misc.IsMultiple(n, prime) {
			fmt.Println("Prime factor: ", prime)
			quotient := n / prime
			if !misc.IsPrime(quotient) {
				return findLargestPrimeFactor(quotient)
			} else {
				return quotient
			}
		}
	}

	return -1
}

func main() {
	fmt.Println("Largest prime factor: ", findLargestPrimeFactor(600851475143))
}
