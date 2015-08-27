// Special Pythagorean triplet
// Problem 9
// A Pythagorean triplet is a set of three natural numbers, a < b < c, for which,

// a**2 + b**2 = c**2
// For example, 32 + 42 = 9 + 16 = 25 = 52.

// There exists exactly one Pythagorean triplet for which a + b + c = 1000.
// Find the product abc.

package main

import (
	"fmt"
)

func findPythagoreanTriplet(n int) (a,b,c int) {
	if n < 6 {
		return -1, -1, -1
	}

	for a := 1; a < n; a++ {
		for b := 1; b < n; b++ {
			for c := 1; c < n; c++ {
				if a < b && b < c && a+b+c == 1000 {
					if a*a+b*b == c*c {
						return a,b,c
					}
				}
			}
		}
	}

	return -1, -1, -1
}

func main() {
	n := 1000

	a, b, c := findPythagoreanTriplet(n)
	fmt.Printf("Pythagorean triplet for %v: %v %v %v\n", n, a, b, c)
	fmt.Println("Product: ", a*b*c)
}
