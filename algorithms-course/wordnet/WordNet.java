import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.DirectedCycle;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;

// This class represents the semantic "is-a" relationships between words using a DAG. An object
// is initialized using a synset file (vertex information) and a hypernym file (edge information)
// and then it allows for queries of the distance between two words through a common ancestor (aka
// shortest ancestor path) and queries of closest common ancestor synset.
public class WordNet {
    static final boolean DEBUG = false;

    private SAP sap;
    private HashMap<String, ArrayList<Integer>> wordToIdsMap;
    private HashMap<Integer, String> idToSynsetMap;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In synsetIn = new In(synsets);
        String[] synsetLines = synsetIn.readAllLines();

        initializeWordMaps(synsetLines);

        In hypernymIn = new In(hypernyms);
        String[] hypernymLines = hypernymIn.readAllLines();
        Digraph graph = buildWordGraph(hypernymLines, synsetLines.length);
        
        if (DEBUG) {
            System.out.println(graph.toString());
        }

	// Verify that the resulting graph is a rooted DAG
        DirectedCycle directedCycle = new DirectedCycle(graph);
        if (directedCycle.hasCycle() || !hasOneRoot(graph)) {
            throw new IllegalArgumentException();
        }
        
        sap = new SAP(graph);
    }

    
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordToIdsMap.keySet();
    }


    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return wordToIdsMap.get(word) != null;
    }


    // distance between nounA and nounB (the length of the shortest ancestor path
    // between the two words)
    public int distance(String nounA, String nounB) {
        return sap.length(wordToIdsMap.get(nounA), wordToIdsMap.get(nounB));
    }


    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        return idToSynsetMap.get(sap.ancestor(wordToIdsMap.get(nounA), wordToIdsMap.get(nounB)));
    }


    private void initializeWordMaps(String[] synsetLines) {
        wordToIdsMap = new HashMap<String, ArrayList<Integer> >();
        idToSynsetMap = new HashMap<Integer, String>();
        for (String line : synsetLines) {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            idToSynsetMap.put(id, parts[1]);
            String[] synonyms = parts[1].split(" ");
            for (String synonym : synonyms) {
                ArrayList<Integer> wordIdList = wordToIdsMap.get(synonym);
                if (wordIdList == null) {
                    wordIdList = new ArrayList<Integer>();
                    wordToIdsMap.put(synonym, wordIdList);
                }
                wordIdList.add(id);
            }
        }
    
        if (DEBUG) {
            System.out.println(wordToIdsMap.toString());
        }
    }


    private Digraph buildWordGraph(String[] hypernymLines, int vertexCount) {
        Digraph graph = new Digraph(vertexCount);
        for (String hypernymStr : hypernymLines) {
            String[] idStrings = hypernymStr.split(",");
            int symsetId = Integer.parseInt(idStrings[0]);
            for (int i = 1; i < idStrings.length; i++) {
                int hypernymId = Integer.parseInt(idStrings[i]);
                graph.addEdge(symsetId, hypernymId);
            }
        }

        return graph;
    }


    // A "root" for this DAG has no outgoing edges. Return true if there is only
    // one such node.
    private boolean hasOneRoot(Digraph g) {
	int rootsCount = 0;
	for (int v = 0; v < g.V(); v++) {
	    if (g.outdegree(v) == 0) {
		rootsCount++;
	    }
	}

	return rootsCount == 1;
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
