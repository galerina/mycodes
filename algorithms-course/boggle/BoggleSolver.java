import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.BitSet;

// This class is initialized with a dictionary of valid strings and can then be used
// to solve BoggleBoards for all possible words. Internally it uses a ternary search
// tree for word lookups.
// I implemented several optimizations to improve performance (and satisfy the algs4 autograder):
// 1) Define custom TST that allows querying whether a prefix is present in the tree
// 2) Implement an iterator for the TST that allows us to keep track of the depth-first
//    search position in the tree at the same time as we are DFS'ing in the boggle board.
//    This eliminates a lot of repetitive work in tracing through redundant prefixes in
//    the TST.
// 3) use array for the first two (or three?) levels of the TST to speed up queries.
// 4) TODO-maybe: use iterative algorithm for board search
public class BoggleSolver {
    private final TST dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new TST();
        for (String s : dictionary) {
            if (s.length() >= 3) {
                this.dictionary.put(s);
            }
        }
    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        // For each board position, sprawl out along each possible path, adding words that
        // are in the dictionary and ending when the prefix is not contained in the dictionary.
        HashSet<String> words = new HashSet<String>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                getWordsHelper(board, dictionary.iterator(), new BitSet(), words, i, j);
            }
        }

        return words;
    }


    private void getWordsHelper(BoggleBoard board, DictionaryIterator tstIter, BitSet marked, HashSet<String> words, int row, int col) {
        marked.set(row * board.cols() + col);
        char letter = board.getLetter(row, col);
        if (letter == 'Q') {
            tstIter.advance('Q');
            tstIter.advance('U');
        } else {
            tstIter.advance(letter);
        }

        if (tstIter.isWord()) {
            words.add(tstIter.getString());
        }

        if (tstIter.isPrefix()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = row + i;
                    int newCol = col + j;
                    if (0 <= newRow && newRow < board.rows() && 
                        0 <= newCol && newCol < board.cols() && !marked.get(newRow * board.cols() + newCol)) {
                        getWordsHelper(board, dictionary.iteratorClone(tstIter), (BitSet) marked.clone(), words, newRow, newCol);
                    }
                }
            }
        }
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dictionary.contains(word)) {
            return 0;
        }

        if (word.length() <= 2) {
            return 0;
        } else if (word.length() <= 4) {
            return 1;
        } else if (word.length() <= 5) {
            return 2;
        } else if (word.length() <= 6) {
            return 3;
        } else if (word.length() <= 7) {
            return 5;
        } else {
            return 11;
        }
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        StdOut.println(board);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);

        if (true) {
            long startTime = System.nanoTime();
            int T = 150;
            int M = 4;
            int N = 4;
            for (int i = 0; i < T; i++) {
                BoggleBoard b = new BoggleBoard(M, N);
                Iterable<String> iter = solver.getAllValidWords(board);
            }
            long endTime = System.nanoTime();
            StdOut.printf("%g ms per solve, %dx%d board\n", (endTime - startTime) * 1.0 / (1000000*T), M, N);
        }
    }
}
