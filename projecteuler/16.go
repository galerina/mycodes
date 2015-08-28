// 2^15 = 32768 and the sum of its digits is 3 + 2 + 7 + 6 + 8 = 26.

// What is the sum of the digits of the number 2^1000?

package main

import(
	"fmt"
	"math/big"
)

func digitSum(n *big.Int) uint32 {
	str := n.String()
	var sum uint32 = 0
	for i := range str {
		sum += uint32(str[i] - '0')
	}

	return sum
}

func main() {
	i := new(big.Int)

	// 2^1000 == 1 << 1000
	i.SetString("1",10)
	i.Lsh(i, 1000)

	fmt.Println(digitSum(j))
}
