import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.HashMap;

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
    private static final boolean DEBUG = false;

    private Digraph digraph;
    private int sapLength;
    private int sapAncestor;


    // Caches: optimization to reduce calls/sec in autograder
    private class Pair {
        private int x;
        private int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        @Override
        public int hashCode() {
            return this.x ^ this.y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            return true;
        }
    }

    // Maps a pair of vertices to length and ancestor
    private HashMap<Pair, Pair> sapCache;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.NullPointerException();
        }
        digraph = new Digraph(G);
        sapCache = new HashMap<Pair, Pair>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!isValidVertex(digraph, v) || !isValidVertex(digraph, w)) {
            throw new IndexOutOfBoundsException();
        }

        calculateSAP(v, w);
        return sapLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!isValidVertex(digraph, v) || !isValidVertex(digraph, w)) {
            throw new IndexOutOfBoundsException();
        }

        
        calculateSAP(v, w);
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

    private class BreadthFirstSearch {
        private Queue<Integer> q;
        private boolean[] marked;
        private int[] distTo;
        private Digraph digraph;

        public BreadthFirstSearch(Digraph g, int startNode) {
            q = new Queue<Integer>();
            digraph = g;
            marked = new boolean[digraph.V()];
            distTo = new int[digraph.V()];

            marked[startNode] = true;
            distTo[startNode] = 0;
            q.enqueue(startNode);
        }

        ArrayList<Integer> frontier() {
            ArrayList<Integer> frontierArr = new ArrayList<Integer>();

            int currentDistance = distTo[q.peek()];
            for (int i : q) {
                if (distTo[i] == currentDistance) {
                    frontierArr.add(i);
                } else {
                    break;
                }
            }

            return frontierArr;
        }

        int height() {
            return distTo[q.peek()];
        }

        void step() {
            // Explore all nodes at the same distance from the origin.
            int currentDistance = distTo[q.peek()];

            if (DEBUG) {
                StdOut.printf("Step: Frontier distance %d\n", currentDistance);
                StdOut.printf("Step: Queue size %d\n", q.size());
            }
            while (!q.isEmpty() && distTo[q.peek()] == currentDistance) {
                int current = q.dequeue();
                if (DEBUG) {
                    StdOut.printf("Exploring %d...\n", current);
                }
                for (int next : digraph.adj(current)) {
                    if (!marked[next]) {
                        distTo[next] = distTo[current] + 1;
                        marked[next] = true;
                        q.enqueue(next);
                    }
                }
            }
        }

        boolean isOver() {
            return q.isEmpty();
        }

        int current() {
            return q.peek();
        }

        int distTo(int v) {
            return distTo[v];
        }

        boolean isMarked(int v) {
            return marked[v];
        }
    }


    // To find the SAP, do a concurrent breadth first search starting from 
    // each node v and w which terminates when we reach a vertex that has 
    // already been marked by the other node.
    private void calculateSAP(int v, int w) {
        if (v == w) {
            sapAncestor = v;
            sapLength = 0;
            return;
        }

        // Impose ordering on vertices so each pair only needs to be cached once
        Pair vertexPair = new Pair(Math.min(v, w), Math.max(v, w));

        if (DEBUG) {
            System.out.println(sapCache);
        }
        Pair cachedResult = sapCache.get(vertexPair);
        if (cachedResult != null) {
            if (DEBUG) {
                StdOut.printf("Reading values from cache...\n");
            }
            sapLength = cachedResult.x;
            sapAncestor = cachedResult.y;
            return;
        }

        BreadthFirstSearch searchFromV = new BreadthFirstSearch(digraph, v);
        BreadthFirstSearch searchFromW = new BreadthFirstSearch(digraph, w);

        // Execute alternating steps for the breadth first searches starting from v and w
        // until we find the common ancestor or until both searches have terminated. Some
        // duplication but this version seemed to be the most readable.
        sapLength = Integer.MAX_VALUE;
        while ((!searchFromV.isOver() && searchFromV.height() < sapLength) ||
               (!searchFromW.isOver() && searchFromW.height() < sapLength)) {
             if (!searchFromV.isOver()) {
                 for (int f : searchFromV.frontier()) {
                    if (searchFromW.isMarked(f)) {
                        int pathLength = searchFromV.distTo(f) + searchFromW.distTo(f);
                        if (pathLength < sapLength) {
                            sapLength = searchFromV.distTo(f) + searchFromW.distTo(f);
                            sapAncestor = f;
                        }
                    }
                }
                if (DEBUG) {
                    StdOut.printf("V search stepping...\n");
                }
                searchFromV.step();
            }

            if (!searchFromW.isOver()) {
                for (int f : searchFromW.frontier()) {
                    if (searchFromV.isMarked(f)) {
                        int pathLength = searchFromV.distTo(f) + searchFromW.distTo(f);
                        if (pathLength < sapLength) {
                            sapLength = searchFromV.distTo(f) + searchFromW.distTo(f);
                            sapAncestor = f;
                        }
                    }
                }
                if (DEBUG) {
                    StdOut.printf("W search stepping...\n");
                }
                searchFromW.step();
            }
        }

        if (sapLength == Integer.MAX_VALUE) {
            // We didn't find a common ancestor
            sapLength = -1;
            sapAncestor = -1;
        }

        // Cache the results
        // Impose ordering on vertices so each pair only needs to be cached once
        vertexPair = new Pair(Math.min(v, w), Math.max(v, w));
        Pair result = new Pair(sapLength, sapAncestor);
        sapCache.put(vertexPair, result);
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
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

        /*
        int[] v = {3, 9, 7, 1};
        int[] w = {11, 12, 2, 6};

        int length   = sap.length(Arrays.asList(v), Arrays.asList(w));
        int ancestor = sap.ancestor(Arrays.asList(v), Arrays.asList(w));
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        */
    }
}
