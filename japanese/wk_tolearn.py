# Query the anki database to get learned Heisig kanji and query the WaniKani API to get learned kanji and output
# the wanikani kanji that are not present in Anki

import sqlite3
import os.path
import json
from urllib2 import Request, urlopen, URLError

anki_db_path = "%s/Documents/Anki/User 2/collection.anki2" % os.path.expanduser('~')

con = sqlite3.connect(anki_db_path)
c = con.cursor()
c.execute("SELECT decks from COL")

# Find id of heisig db
decks = json.loads(c.fetchone()[0])
heisig_deck = next(deck for deck in decks.values() if deck["name"] == "heisig")

c.execute("SELECT flds from notes where id in (SELECT nid from cards WHERE did = ?)", (heisig_deck["id"],))
res = c.fetchall()
heisig_kanji = [x[0].split(u'\x1f')[1] for x in res]

# Retrieve wani kani kanji

kanji_api_url = "https://www.wanikani.com/api/user/04b0e939e046342d5a0c4dee70126067/kanji"
request = Request(kanji_api_url)

try:
    response = urlopen(request)
    kanji_data = json.loads(response.read())
    wani_kani_kanji = [x["character"] for x in kanji_data["requested_information"]]
except URLError, e:
    print 'No kittez. Got an error code:', e

# Print wani kani kanji that haven't been added to Anki yet
for kanji in wani_kani_kanji:
    if not kanji in heisig_kanji:
        print kanji
