package main

import (
	"japanese"
	"strings"
	"fmt"
)

// Lists the kanji in Heisig order and outputs whether they have been learned in Minna No Nihongo or not

func main() {
	minnaKanji := strings.Join(japanese.MinnaKanji, "")
	for i, kanji := range japanese.HeisigKanji {
		if idx := strings.Index(minnaKanji, kanji); idx != -1 {
			fmt.Printf("%d:%s *** Learned as MNN %d\n", i, kanji, idx/3)
		} else {
			fmt.Printf("%d:%s\n", i, kanji)
		}
	}
}
