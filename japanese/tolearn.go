package main

import (
	"japanese"
	"fmt"
)

func pos(slice []string, value string) int {
    for p, v := range slice {
        if (v == value) {
            return p
        }
    }
    return -1
}


// Find the Minna kanji that aren't in the Anki deck yet and print them out in 
// the Remembering the Kanji order.
func main() {
	unlearned := []string{}

	for _, mnnCharacter := range japanese.MinnaKanji {
		learned := false;
		for _,aCharacter := range japanese.AnkiKanji {
			if aCharacter == mnnCharacter {
				learned = true
				break
			}
		}

		if !learned {
			unlearned = append(unlearned, mnnCharacter)
		}
	}

	fmt.Printf("%d unlearned kanji\n", len(unlearned))

	for hIdx, hCharacter := range japanese.HeisigKanji {
		if pos(unlearned, hCharacter) != -1 {
			fmt.Printf("%s: %d\n", hCharacter, hIdx+1)
		}
	}
}
