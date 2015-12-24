import collections
import codecs
import japanese

# Things to find out:
#  Most common kanji
#  Most common 2, 3, 4, 5 character sequences; are these words?

def WordScore(frequency, length):
    return frequency * (pow(length,3)) / 2.0

def GetScoreDictionary(histogram):
    score_dictionary = collections.defaultdict(list)

    for entry in histogram:
        score = WordScore(histogram[entry], len(entry))
        score_dictionary[score].append(entry)

    return score_dictionary

def PrintNHighest(score_dict, n):
    for i in sorted(score_dict.keys(),reverse=True)[:n]:
        for word in score_dict[i]:
            print(word,end=",")
            print(":",end="")
            print(i)

data_dir="scripts"

import os

lines = []
for filename in ['noruwei.txt']:
# for filename in os.listdir(data_dir):
    full_filename = os.path.join(data_dir, filename)
    print("Loading %s..."%full_filename)
    f = codecs.open(full_filename, encoding='utf-8')
    lines += f.readlines()

word_histogram = collections.defaultdict(int)
kanji_histogram = collections.defaultdict(int)

min_word_size = 2
max_word_size = 6

for line in lines:
    for window_size in range(min_word_size, max_word_size+1):
        index = 0
        while index + window_size <= len(line):
            s = line[index:index+window_size]
            if japanese.is_japanese_text(s):
                # print(repr(s))
                word_histogram[s] += 1
            index += 1

    for c in line:
        if japanese.iskanji(c):
            kanji_histogram[c] += 1

n = 2000
word_score_dictionary = GetScoreDictionary(word_histogram)
print("Printing %d highest scoring words:"%n)
PrintNHighest(word_score_dictionary,n)

kanji_score_dictionary = GetScoreDictionary(kanji_histogram)
print("Printing %d highest scoring words:"%n)
PrintNHighest(kanji_score_dictionary,n)

