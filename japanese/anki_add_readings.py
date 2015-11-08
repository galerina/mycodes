import sqlite3
import os.path
from urllib2 import Request, urlopen, URLError
import json

# Get readings for the kanji from wanikani
def get_readings_map():
    levels_string = ",".join([str(x) for x in range(1,61)])
    kanji_api_url = "https://www.wanikani.com/api/user/04b0e939e046342d5a0c4dee70126067/kanji/%s"%levels_string
    request = Request(kanji_api_url)

    readings_map = {}
    try:
        response = urlopen(request)
        kanji_data = json.loads(response.read())
        for x in kanji_data["requested_information"]:
            readings_map[x["character"]] = x[x["important_reading"]]
    except URLError, e:
        print 'Got an error code:', e

    return readings_map

def main(): 
    readings_map = get_readings_map()

    # Access anki database and update entries to contain readings
    anki_db_path = "%s/Documents/Anki/User 2/collection.anki2" % os.path.expanduser('~')

    con = sqlite3.connect(anki_db_path)
    c = con.cursor()
    c.execute("SELECT decks from COL")

    # Find id of heisig db
    decks = json.loads(c.fetchone()[0])
    heisig_deck = next(deck for deck in decks.values() if deck["name"] == "heisig")

    c.execute("SELECT id,flds,sfld from notes where id in (SELECT nid from cards WHERE did = ?)", (heisig_deck["id"],))
    res = c.fetchall()
    field_sepchar = u'\x1f'
    for id, flds, sfld in res:
        keyword, anki_kanji = flds.split(field_sepchar)
        
        if "(" not in keyword and readings_map.has_key(anki_kanji):
            new_keyword = "%s (%s)"%(keyword,readings_map[anki_kanji])
            query = "UPDATE notes SET flds = '%s', sfld = '%s' where id = %d"%(field_sepchar.join([new_keyword, anki_kanji]), new_keyword, id)
            print query
            c.execute("UPDATE notes SET flds = ?, sfld = ? where id = ?", (field_sepchar.join([new_keyword, anki_kanji]), new_keyword, id))

    con.commit()
    con.close()

if __name__=="__main__":
    main()
