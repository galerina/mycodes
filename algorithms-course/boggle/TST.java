/******************************************************************************
 *  Compilation:  javac TST.java
 *  Execution:    java TST < words.txt
 *  Dependencies: StdIn.java
 *
 *  This is a modified version of the TST class found at:
 *    http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/TST.java.html
 *  Since I used this class as a dictionary for a Boggle solver, I added some
 *  optimizations that speed up this use case:
 *   -Use R*R-way trie style lookup for the first two characters in a string
 *   -Implement an iterator so that the DFS-state in the Boggle board can be
 *    stored rather than repeating the first part of the string lookup many
 *    times.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class TST {
    private int N;              // size
    private Node[] root;        // root of TST

    private static final int ALPHABET_SIZE = 'Z' - 'A' + 1;

    private static class Node {
        private char c;                        // character
        private Node left, mid, right;  // left, middle, and right subtries
        private boolean isWord;
    }

    private class TSTIterator implements DictionaryIterator {
        // It feels dirty that we have to store the root of the tree in the
        // iterator but this is a consequence of using R-way lookup for the
        // first characters.
        private Node[] root;
        private Node current;
        private String currentString = "";

        public boolean advance(char c) {
            currentString += c;
            if (currentString.length() == 1) {
                return true;
            } else if (currentString.length() == 2) {
                current = root[TST.getRootIndex(currentString)];
                return (current == null) ? false : true;
            }

            if (current == null) {
                return false;
            }

            current = current.mid;
            while (current != null) {
                if (c < current.c) {
                    current = current.left;
                } else if (c > current.c) {
                    current = current.right;
                } else {
                    return true;
                }
            }

            return false;
        }

        public boolean isWord() {
            return (current != null && current.isWord);
        }

        public boolean isPrefix() {
            return (currentString.length() <= 1 || 
                    (current != null && current.mid != null));
        }

        public String getString() {
            return currentString;
        }
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TST() {
        root = new Node[ALPHABET_SIZE*ALPHABET_SIZE];
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return N;
    }

    public DictionaryIterator iterator() {
        TSTIterator iter = new TSTIterator();
        iter.current = null;
        iter.root = root;
        return iter;
    }

    public DictionaryIterator iteratorClone(DictionaryIterator iter) {
        TSTIterator newIter = new TSTIterator();
        TSTIterator oldIter = (TSTIterator) iter;
        newIter.root = oldIter.root;
        newIter.current = oldIter.current;
        newIter.currentString = oldIter.currentString;
        return newIter;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and
     *     <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(String key) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = getSubtrie(key);
        if (x == null) return false;
        return x.isWord;
    }


    /**
     * Returns the subtree for the parameter key, null if this tree is not a word or prefix
     */
    private Node getSubtrie(String key) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");

        Node currentNode = root[getRootIndex(key)];
        if (currentNode == null) {
            return null;
        } else {
            currentNode = currentNode.mid;
        }

        int d = 2;
        while (true) {
            if (currentNode == null) {
                return null;
            }

            char c = key.charAt(d);
            if (c < currentNode.c) {
                currentNode = currentNode.left;
            } else if (c > currentNode.c) {
                currentNode = currentNode.right;
            } else if (d < key.length() - 1) {
                currentNode = currentNode.mid;
                d++;
            } else {
                return currentNode;
            }
        }
    }


    /**
     * Inserts the key into the symbol table
     * @param key the key
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void put(String key) {
        if (!contains(key)) N++;

        char c = key.charAt(0);
        Node x = root[getRootIndex(key)];
        root[getRootIndex(key)] = put(x, key, 1);
    }

    private Node put(Node x, String key, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
            x.isWord = false;
        }
        if (c < x.c) {
            x.left  = put(x.left,  key, d);
        } else if (c > x.c) {
            x.right = put(x.right, key, d);
        } else if (d < key.length() - 1) {
            x.mid   = put(x.mid,   key, d+1);
        } else {
            x.isWord   = true;
        }
        return x;
    }

    /**
     * Return the index into the root array for the parameter key
     */
    private static int getRootIndex(String key) {
        return ((key.charAt(0) - 'A') * ALPHABET_SIZE) + (key.charAt(1) - 'A');
    }
}

/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
