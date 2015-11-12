import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

// Quoted from http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html:
//
//   ...An ancestral path between two vertices v and w in a digraph is a directed path
//   from v to a common ancestor x, together with a directed path from w to the same
//   ancestor x. A shortest ancestral path is an ancestral path of minimum total length...
//
// The SAP class is initialized with a directed graph and it allows a user to query  
// information about the the shortest ancestral path for any two nodes in that graph.  
// The methods are length(), which returns the length of the SAP, and ancestor(), which
// returns the ancestor vertex.
public class SAP {
    private Digraph digraph;
    private int sapLength;
    private int sapAncestor;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.NullPointerException();
        }
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
	if (!isValidVertex(digraph, v) || !isValidVertex(digraph, w)) {
	    throw new IndexOutOfBoundsException();
	}
	calculateSAP(v,w);
	return sapLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
	if (!isValidVertex(digraph, v) || !isValidVertex(digraph, w)) {
	    throw new IndexOutOfBoundsException();
	}
	calculateSAP(v,w);
	return sapAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
	if (v == null || w == null) {
	    throw new NullPointerException();
	}

	calculateSAP(v, w);
	return sapLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
	if (v == null || w == null) {
	    throw new NullPointerException();
	}

	calculateSAP(v, w);
	return sapAncestor;
    }

    private void calculateSAP(int v, int w) {
	if (v == w) {
	    sapAncestor = v;
	    sapLength = 0;
	    return;
	}

	// Do a concurrent breadth first search starting from each node v and w
	// which terminates when we reach a vertex that has already been marked 
	// by the other node. Maintain separate marked and distTo arrays for
	// each vertex
	Queue<Integer> vq = new Queue<Integer>();
	Queue<Integer> wq = new Queue<Integer>();

	boolean[] vMarked = new boolean[digraph.V()];
	boolean[] wMarked = new boolean[digraph.V()];
	int[] vDistTo = new int[digraph.V()];
	int[] wDistTo = new int[digraph.V()];

	vMarked[v] = true;
	vDistTo[v] = 0;
	vq.enqueue(v);

	wMarked[w] = true;
	wDistTo[w] = 0;
	wq.enqueue(w);

	while (!vq.isEmpty() || !wq.isEmpty()) {
	    if (!vq.isEmpty()) {
		// v search
		int current = vq.dequeue();
		if (wMarked[current]) {
		    sapLength = vDistTo[current] + wDistTo[current];
		    sapAncestor = current;
		    return;
		}
		for (int next : digraph.adj(current)) {
		    if (!vMarked[next]) {
			vDistTo[next] = vDistTo[current] + 1;
			vMarked[next] = true;
			vq.enqueue(next);
		    }
		}
	    }

	    if (!wq.isEmpty()) {
		// w search
		int current = wq.dequeue();
		if (vMarked[current]) {
		    sapLength = vDistTo[current] + wDistTo[current];
		    sapAncestor = current;
		    return;
		}
		for (int next : digraph.adj(current)) {
		    if (!wMarked[next]) {
			wDistTo[next] = wDistTo[current] + 1;
			wMarked[next] = true;
			wq.enqueue(next);
		    }
		}
	    }
	}

	// We didn't find a common ancestor
	sapLength = -1;
	sapAncestor = -1;
    }


    // Calculate SAP across sets of v and w, looking for the shortest combination
    private void calculateSAP(Iterable<Integer> vIterable, Iterable<Integer> wIterable) {
	int minLength = Integer.MAX_VALUE;
	int minAncester = -1; 
	for (int v : vIterable) {
	    for (int w : wIterable) {
		calculateSAP(v, w);
		if (sapLength != -1 && sapLength < minLength) {
		    minLength = sapLength;
		    minAncester = sapAncestor;
		}
	    }
	}

	if (minLength == Integer.MAX_VALUE) {
	    sapLength = -1;
	    sapAncestor = -1;
	} else {
	    sapLength = minLength;
	    sapAncestor = minAncester;
	}
    }


    // Check that g contains vertex v
    private static boolean isValidVertex(Digraph g, int v) {
	return (0 <= v && v < g.V());
    }


    // do unit testing of this class
    public static void main(String[] args) {
	In in = new In(args[0]);
	Digraph G = new Digraph(in);
	SAP sap = new SAP(G);
	while (/*false &&*/ !StdIn.isEmpty()) {
	    int v = StdIn.readInt();
	    int w = StdIn.readInt();
	    int length   = sap.length(v, w);
	    int ancestor = sap.ancestor(v, w);
	    StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	}

	Integer v[] = {3, 9, 7, 1};
	Integer w[] = {11, 12, 2, 6};

	int length   = sap.length(Arrays.asList(v), Arrays.asList(w));
	int ancestor = sap.ancestor(Arrays.asList(v), Arrays.asList(w));
	StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
