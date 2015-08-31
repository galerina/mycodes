// n! means n × (n − 1) × ... × 3 × 2 × 1

// For example, 10! = 10 × 9 × ... × 3 × 2 × 1 = 3628800,
// and the sum of the digits in the number 10! is 3 + 6 + 2 + 8 + 8 + 0 + 0 = 27.

// Find the sum of the digits in the number 100!

package main

import (
	"math/big"
	"misc"
	"fmt"
)

func factorial(n *big.Int) *big.Int {
	product := big.NewInt(1)
	one := big.NewInt(1)

	for counter := big.NewInt(1); counter.Cmp(n) <= 0; counter.Add(counter,one) {
		product.Mul(product,counter)
	}

	return product
}

func main() {
	fmt.Println(misc.DigitSum(factorial(big.NewInt(100))))
}
