package main

import (
	"japanese"
	"io/ioutil"
	"strings"
	"fmt"
)

var filename string = "heisig.txt"

func main() {
	content, err := ioutil.ReadFile(filename)
	if err != nil {
		fmt.Println("Error in reading the file")
	}

	lines := strings.Split(string(content), "\n")

	ankiKanji := ""
	for _,line := range lines {
		parts := strings.Fields(line)
		if len(parts) == 2 {
			ankiKanji = ankiKanji + parts[1]
		}
	}

	for hIdx,hCharacter := range japanese.HeisigKanji {
		if !strings.Contains(ankiKanji, hCharacter) {
			fmt.Printf("Missing: %d %s\n", hIdx+1, hCharacter)
		}
	}

}
