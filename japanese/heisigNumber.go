package main

import (
	"japanese"
	"fmt"

)

func main() {
	for mnnIdx, mnnCharacter := range japanese.MinnaKanji {
		for hIdx,hCharacter := range japanese.HeisigKanji {
			if hCharacter == mnnCharacter {
				fmt.Printf("MNN #%d: %s : Heisig #%d\n", mnnIdx+1, hCharacter, hIdx+1)
			}
		}
	}
}
