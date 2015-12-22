var readline = require('readline');
var rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
    terminal: false
});

var entryRE = /(.)[^{]+ F([^ ]+)[^{]+{([^}]+)}/;
var entries = [];

rl.on('line', function(line) {
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

rl.on('close', function() {
    console.log(entries);
});
