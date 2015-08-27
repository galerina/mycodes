// Largest palindrome product
// Problem 4
// A palindromic number reads the same both ways. The largest palindrome made from the product of two 2-digit numbers is 9009 = 91 × 99.

// Find the largest palindrome made from the product of two 3-digit numbers.

package main

import (
	"fmt"
	"strconv"
	"time"
)

func isPalindrome(s string) bool {
	for i,j := 0,len(s)-1; i < j; i, j = i+1, j-1 {
		if s[i] != s[j] {
			return false
		}
	}

	return true
}

func isPalindromicNumber(n int) bool {
	return isPalindrome(strconv.Itoa(n))
}

func main() {
	start := time.Now()
	lowLimit := 100
	hiLimit := 999
	largestProduct := 0
	for i := lowLimit; i <= hiLimit; i++ {
		for j, product := lowLimit, lowLimit * i; j <= hiLimit; j, product = j + 1, product + i {
			if isPalindromicNumber(product) && product > largestProduct {
				largestProduct = product
			}
		}
	}

	fmt.Println("Largest palindromic product: ", largestProduct)
	fmt.Println("Time elapsed: ", time.Since(start))
}
