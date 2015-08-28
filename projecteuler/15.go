// Starting in the top left corner of a 2×2 grid, and only being able to move to the right and down, there are exactly 6 routes to the bottom right corner.

// How many such routes are there through a 20×20 grid?

package main

import (
	"fmt"
)

func routes(x,y int) int {
	// Allocate the top-level slice.
	routeArray := make([][]int, x+1) // One row per unit of y.

	// Loop over the rows, allocating the slice for each row.
	for i := range routeArray {
		routeArray[i] = make([]int, y+1)
	}

	// Routes for (i, 0) and (0, i) for any i only have one
	// route.
	for i := range routeArray {
		routeArray[i][0] = 1
	}

	for i := range routeArray[0] {
		routeArray[0][i] = 1
	}

	// Build up an array whose (i,j)th element is the number of paths for an i x j rectangle
	for i := 1; i < len(routeArray); i++ {
		for j := 1; j < len(routeArray[i]); j++ {
			routeArray[i][j] = routeArray[i-1][j] + routeArray[i][j-1]
		}
	}

	return routeArray[x][y]
}

func main() {
	x := 20
	y := 20
	fmt.Printf("Lattice paths %dx%d: %d\n", x, y, routes(x,y))
}
