// Smallest multiple
// Problem 5
// 2520 is the smallest number that can be divided by each of the numbers from 1 to 10 without any remainder.

// What is the smallest positive number that is evenly divisible by all of the numbers from 1 to 20?

// 10 = 2 * 5
// 11 = prime
// 12 = 2 * 2 * 3
// 13 = prme
// 14 = 2 * 7
// 15 = 3 * 5
// 16 = 2 * 2 * 2 * 2
// 17 = prime
// ...
//
// Smallest multiple of a set of numbers =
// (p1 ** x1) * ... * (pn ** xn) where pi are the prime factors in the numbers and xi is the highest power
// of a prime factor contained in any of the numbers.

package main

import (
	"fmt"
	"time"

	"misc"
)


func mergeFactors(a,b map[int]int) map[int]int {
	newFactors := map[int]int{}
	for key, _ := range a {
		newFactors[key] = misc.Max(a[key], b[key])
	}

	for key, _ := range b {
		newFactors[key] = misc.Max(a[key], b[key])
	}

	return newFactors
}

func multiplyFactors(factors map[int]int) int {
	product := 1
	for key, val := range factors {
		for i := 0; i < val; i++ {
			fmt.Println("Factor: ", key)
			product *= key
		}
	}

	return product
}

func findSmallestMultiple(numbers []int) int {
	factors := map[int]int{}
	for _, n := range numbers {
		nFactors := misc.GetPrimeFactorsMap(n)
		factors = mergeFactors(factors, nFactors)
	}

	return multiplyFactors(factors)
}



func main() {
	start := time.Now()
	var n []int
	lowerLimit := 1
	upperLimit := 20
	for i := lowerLimit; i <= upperLimit; i++ {
		n = append(n, i)
	}

	fmt.Println("Time for append: ", time.Since(start))

	start = time.Now()
	fmt.Printf("Smallest multiple (%v to %v): %v\n", lowerLimit, upperLimit, findSmallestMultiple(n))
	fmt.Println("Time for find smallest: ", time.Since(start))	
}
