// If the numbers 1 to 5 are written out in words: one, two, three, 
// four, five, then there are 3 + 3 + 5 + 4 + 4 = 19 letters used 
// in total.

// If all the numbers from 1 to 1000 (one thousand) inclusive were 
// written out in words, how many letters would be used?


// NOTE: Do not count spaces or hyphens. For example, 342 (three 
// hundred and forty-two) contains 23 letters and 115 (one hundred 
// and fifteen) contains 20 letters. The use of "and" when writing 
// out numbers is in compliance with British usage.

package main

import (
	"fmt"
	"strings"
)

var numberNames [20]string = [...]string{
	"zero",
	"one",
	"two",
	"three",
	"four",
	"five",
	"six",
	"seven",
	"eight",
	"nine",
	"ten",
	"eleven",
	"twelve",
	"thirteen",
	"fourteen",
	"fifteen",
	"sixteen",
	"seventeen",
	"eighteen",
	"nineteen",
}

var multiplesOfTenNames [10]string = [...]string {
	"zero",
	"ten",
	"twenty",
	"thirty",
	"forty",
	"fifty",
	"sixty",
	"seventy",
	"eighty",
	"ninety",
}

// 

func numberToEnglishString(n uint) string {
	parts := []string{}

	for n > 0 {
		if n >= 1000 {
			parts = append(parts, "one thousand")
			n %= 1000
		} else if n >= 100 {
			// Get hundreds digit
			digit := n / 100
			hundredsString := strings.Join([]string{numberNames[digit],"hundred"}," ")
			parts = append(parts, hundredsString)

			n %= 100

			if n != 0 {
				parts = append(parts, "and")
			}
		} else if n >= 20 {
			digit := n / 10

			parts = append(parts,multiplesOfTenNames[digit])

			n %= 10
		} else {
			parts = append(parts,numberNames[n])
			n = 0
		}
	}

	return strings.Join(parts," ")
}

func countLetters(s string) int {
	count := 0
	s = strings.ToLower(s)
	for _,c := range s {
		if 'a' <= c && c <= 'z' {
			count++
		}
	}

	return count
}

func main() {
	fmt.Println(numberToEnglishString(917))

	count := 0
	for i:=1;i<=1000;i++ {
		count += countLetters(numberToEnglishString(uint(i)))
	}

	fmt.Println(count)
}

