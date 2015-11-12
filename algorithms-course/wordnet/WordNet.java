import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.util.HashMap;
import java.util.ArrayList;

public class WordNet {
    static final boolean DEBUG = false;

    private SAP sap;
    private HashMap<String, ArrayList<Integer>> wordToIdListMap;
    private HashMap<Integer, String> idToSynsetMap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
	In synsetIn = new In(synsets);
	String[] synsetLines = synsetIn.readAllLines();

	wordToIdListMap = new HashMap<String, ArrayList<Integer> >();
	idToSynsetMap = new HashMap<Integer, String>();
	for (String line : synsetLines) {
	    String[] parts = line.split(",");
	    int id = Integer.parseInt(parts[0]);
	    idToSynsetMap.put(id, parts[1]);
	    String[] synonyms = parts[1].split(" ");
	    for (String synonym : synonyms) {
		ArrayList<Integer> wordIdList = wordToIdListMap.get(synonym);
		if (wordIdList == null) {
		    wordIdList = new ArrayList<Integer>();
		    wordToIdListMap.put(synonym, wordIdList);
		}
		wordIdList.add(id);
	    }
	}

	if (DEBUG) {
	    System.out.println(wordToIdListMap.toString());
	}

	Digraph graph = new Digraph(synsetLines.length);
	In hypernymIn = new In(hypernyms);
	String[] hypernymLines = hypernymIn.readAllLines();
	for (String hypernymStr : hypernymLines) {
	    String[] idStrings = hypernymStr.split(",");
	    int symsetId = Integer.parseInt(idStrings[0]);
	    for (int i = 1; i < idStrings.length; i++) {
		int hypernymId = Integer.parseInt(idStrings[i]);
		graph.addEdge(symsetId, hypernymId);
	    }
	}

	if (DEBUG) {
	    System.out.println(graph.toString());
	}

	sap = new SAP(graph);
    }

   // returns all WordNet nouns

   public Iterable<String> nouns() {
       return wordToIdListMap.keySet();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
       return wordToIdListMap.get(word) != null;
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
       return sap.length(wordToIdListMap.get(nounA), wordToIdListMap.get(nounB));
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
       return idToSynsetMap.get(sap.ancestor(wordToIdListMap.get(nounA), wordToIdListMap.get(nounB)));
   }

   // do unit testing of this class
   public static void main(String[] args) {
       WordNet wordnet = new WordNet(args[0], args[1]);
       while (!StdIn.isEmpty()) {
	   String nounA = StdIn.readString();
	   String nounB = StdIn.readString();
	   int length   = wordnet.distance(nounA, nounB);
	   String synset = wordnet.sap(nounA, nounB);
	   StdOut.printf("length = %d, ancestor = %s\n", length, synset);
       }

   }
}
