// Longest Collatz sequence
// Problem 14
// The following iterative sequence is defined for the set of positive integers:
// 
// n → n/2 (n is even)
// n → 3n + 1 (n is odd)
// 
// Using the rule above and starting with 13, we generate the following sequence:
// 
// 13 → 40 → 20 → 10 → 5 → 16 → 8 → 4 → 2 → 1
// It can be seen that this sequence (starting at 13 and finishing at 1) contains 10 terms. Although it has not been proved yet (Collatz Problem), it is thought that all starting numbers finish at 1.
// 
// Which starting number, under one million, produces the longest chain?
// 
// NOTE: Once the chain starts the terms are allowed to go above one million.

package main

import (
	"fmt"
	"time"
	"misc"
)

func collatz(n int) int {
	if misc.IsMultiple(n, 2) {
		return n/2
	} else {
		return 3*n+1
	}
}

func getCollatzCounter() (func(int) (int)) {
	collatzMap := map[int]int{}

	return func(n int) int {
		remembered, ok := collatzMap[n]
		if ok {
			return remembered
		}

		termsCount := 0
		for termsCount = 1; n != 1; n, termsCount = collatz(n), termsCount+1  {
			remembered, ok = collatzMap[n]
			if ok {
				return termsCount + remembered
			}
		}

		return termsCount
	}
}

func main() {
	start := time.Now()
	countCollatz := getCollatzCounter()

	n := 1000000
	maxI, maxTerms := 0, 0
	for i := 1; i < n; i++ {
		collatzTerms := countCollatz(i)
		if  collatzTerms > maxTerms {
			maxI, maxTerms = i, collatzTerms
		}
	}

	fmt.Printf("Max collatz terms for i under %v: i = %v, terms = %v\n", n, maxI, maxTerms)
	fmt.Println(time.Since(start))
}
