package main

import (
	"io/ioutil"
	"strings"
	"fmt"
	"strconv"
)

var filename string = "heisig-data.txt"

func main() {
	content, err := ioutil.ReadFile(filename)
	if err != nil {
		fmt.Println("Error in reading the file")
	}
	lines := strings.Split(string(content), "\n")

	kanjiMap := make(map[int]string)
	for _,line := range lines {
		parts := strings.Split(line, ":")

		if len(parts) == 2 {
			i, _ := strconv.Atoi(parts[0])
			kanjiMap[i] = parts[1]
		}
	}

	for index := 1; ; index++ {
		if val, ok := kanjiMap[index]; ok {
			fmt.Println(val)
		} else {
			fmt.Printf("Last index was %d\n", index)
			break
		}
	} 
}
