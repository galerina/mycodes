package main

import (
	"fmt"
	"net/http"
	"io/ioutil"
	"encoding/json"
	"japanese"
	"strings"
	"sort"
//    "os"
)

type Word struct {
	Writing string
	Reading string
}

// ByReading implements sort.Interface for []Word based on
// the Reading field.
type ByReading []Word

func (a ByReading) Len() int           { return len(a) }
func (a ByReading) Swap(i, j int)      { a[i], a[j] = a[j], a[i] }
func (a ByReading) Less(i, j int) bool { return a[i].Reading < a[j].Reading }

// ByWriting implements sort.Interface for []Word based on
// the Writing field.
type ByWriting []Word

func (a ByWriting) Len() int           { return len(a) }
func (a ByWriting) Swap(i, j int)      { a[i], a[j] = a[j], a[i] }
func (a ByWriting) Less(i, j int) bool { return a[i].Writing < a[j].Writing }

var apiUrl string = "http://beta.jisho.org/api/v1/search/words?keyword="

func getCommonWords(s string) []Word {
	queryUrl := apiUrl + s + "%20%23common"
	resp, err := http.Get(queryUrl)
	if err != nil {
		// fmt.Println("errro:", err)
	}

	jsonResult, _ := ioutil.ReadAll(resp.Body)

	var data map[string][]map[string][]map[string]string
	err = json.Unmarshal(jsonResult, &data)
	if err != nil {
		// fmt.Println("error:", err)
	}

	words := []Word{}
	for _, v := range data["data"] {
		if j := v["japanese"]; j != nil {
			listing := j[0]
			if writing,reading := listing["word"],listing["reading"]; writing != "" && reading != "" {
				words = append(words, Word{writing,reading} )
			}
			/*
			for _,listing := range j {
				if k := listing["word"]; k != "" {
					words = append(words, k)
				}
			}
                        */
		}
	}

	return words
}

func wordHasNewKanji(word string, learnedKanji string) bool {
	for _,character := range(word) {
		if japanese.IsKanji(string(character)) && strings.Index(learnedKanji, string(character)) == -1 {
			return true
		}
	}

	return false
}

func main() {
	// Find all common words that use only kanji that have been studied
	commonWordsMap := make(map[Word]bool)
	first, last := 309, 336
	//learnedKanji := japanese.HeisigKanji[0:last]
	learnedKanji := japanese.MinnaKanji[0:last]
	for _, character := range(learnedKanji[first:]) {
		for _, word := range(getCommonWords(character)) {
			if !wordHasNewKanji(word.Writing, strings.Join(learnedKanji,"")) {
				commonWordsMap[word] = true
			}
		}
	}

	commonWords := make([]Word, 0, len(commonWordsMap))
	for k := range(commonWordsMap) {
		commonWords = append(commonWords, k)
	}

	sort.Sort(ByReading(commonWords))
	
	for _, word := range(commonWords) {
		fmt.Printf("%s >>>  %s\n", word.Writing, word.Reading)
		fmt.Println()
	}
}
