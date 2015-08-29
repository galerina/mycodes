package main

import (
	"fmt"
	"strings"
	"strconv"
	"misc"
	"io/ioutil"
)

// By starting at the top of the triangle below and moving to adjacent numbers 
// on the row below, the maximum total from top to bottom is 23.

var test_string string = `3
7 4
2 4 6
8 5 9 3`
// 
// That is, 3 + 7 + 4 + 9 = 23.
// 
// Find the maximum total from top to bottom of the triangle below:
// 
var problem_string string = `75
95 64
17 47 82
18 35 87 10
20 04 82 47 65
19 01 23 75 03 34
88 02 77 73 07 63 67
99 65 04 28 06 16 70 92
41 41 26 56 83 40 80 70 33
41 48 72 33 47 32 37 16 94 29
53 71 44 65 25 43 91 52 97 51 14
70 11 33 28 77 73 17 78 39 68 17 57
91 71 52 38 17 14 91 43 58 50 27 29 48
63 66 04 68 89 53 67 30 73 16 69 87 40 31
04 62 98 27 23 09 70 98 73 93 38 53 60 04 23`

// 
// NOTE: As there are only 16384 routes, it is possible to solve this problem by trying every route. 
// However, Problem 67, is the same challenge with a triangle containing one-hundred rows; it cannot be solved by brute force, and requires a clever method! ;o)

// This method works for Problem 67, too :)

func printTriangle(triangle [][]int) {
	for _,row := range triangle {
		for _,elem := range row {
			fmt.Printf("%d ",elem)
		}

		fmt.Println()
	}
}

func maxTotal(triStr string) int {
	lines := strings.Split(triStr, "\n")

	triangle := [][]int{}
	for _,line := range lines {
		newRow := []int{}

		elements := strings.Split(line, " ")

		for _,elem := range elements {
			x,err := strconv.Atoi(elem)
			if err == nil {
				newRow = append(newRow, x)
			}
		}
		
		if len(newRow) > 0 {
			triangle = append(triangle,newRow)
		}
	}

	// Transform the triangle so that any cell will contain the maximum value
	// of a downward path starting at that point
	for i := len(triangle) - 2; i >= 0; i -- {
		for j, _ := range triangle[i] {
			left := triangle[i+1][j]
			right := triangle[i+1][j+1]
			
			triangle[i][j] += misc.Max(left,right)
		}
	}

	// Return the maximum path starting from the top of the triangle
	return triangle[0][0]
}

func main() {
	// Problem 18
	fmt.Println(maxTotal(problem_string))

	// Problem 67
	bytes, _ := ioutil.ReadFile("p067_triangle.txt")
	fmt.Println(maxTotal(string(bytes)))
}
