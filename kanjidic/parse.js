var _ = require('lodash');
var fs = require('fs');
var readline = require('readline');

var dictFile = "kanjidic.txt";
var rlDict = readline.createInterface({
    input: fs.createReadStream(dictFile),
    terminal: false
});

var entryRE = /(.)[^{]+ F([^ ]+)[^{]+{([^}]+)}/;
var entries = [];
var radicalEntries = [];

var merge = _.after(2, function() {
    for (var i = 0; i < entries.length; i++) {
	if (_.has(radicalEntries, entries[i].kanji)) {
	    var radicals = radicalEntries[entries[i].kanji];
	    entries[i]["radicals"] = radicals;
	    entries[i]["radicalMeanings"] = _.compact(radicals.map(function(elem) {
		var radicalDictEntry = undefined;
		if (!_.isEqual(entries[i], elem)) {
		    var radicalDictEntry = _.find(entries, function(entry) {
			return _.isEqual(entry.kanji, elem);
		    });
		}

		if (!_.isEmpty(radicalDictEntry)) {
		    return radicalDictEntry["meaning"];
		} else {
		    return undefined;
		}
	    }));
	}
    }

    // console.log(entries.slice(0, 5));
    console.log(JSON.stringify(entries));
});

rlDict.on('line', function(line) {
    var FREQUENCY_THRESHOLD = 2500;
    var match;
    if (match = entryRE.exec(line)) {
	frequency = Number(match[2]);
	if (frequency < FREQUENCY_THRESHOLD) {
	    entries.push({ kanji: match[1], 
			   frequencyRank: frequency,
			   meaning: match[3]});
	}
    }
});

rlDict.on('close', function() {
    merge();
});

var radFile = "kradfile-u.txt";
var rlRad = readline.createInterface({
    input: fs.createReadStream(radFile),
    terminal: false
});

rlRad.on('line', function(line) {
    parts = line.split(' : ');
    if (_.eq(parts.length, 2)) {
	radicalEntries[parts[0]] = parts[1].split(" ");
    }
});

rlRad.on('close', function() {
    merge();
});

