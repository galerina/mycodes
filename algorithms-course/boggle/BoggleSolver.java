import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.TreeSet;
import java.util.BitSet;

public class BoggleSolver {
    private final TST<Boolean> dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new TST<Boolean>();
        for (String s : dictionary) {
            this.dictionary.put(s, true);
        }

        System.out.println(this.dictionary.keysWithPrefix("test"));
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        // For each board position, sprawl out along each possible path, adding words that
        // are in the dictionary and ending when the prefix is not contained in the dictionary.
        TreeSet<String> words = new TreeSet<String>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                getWordsHelper(board,new BitSet(),words,"",i,j);
            }
        }

        return words;
    }

    private void getWordsHelper(BoggleBoard board, BitSet marked, TreeSet<String> words, String prefix, int row, int col) {
        marked.set(row * board.cols() + col);
        String newPrefix = prefix + board.getLetter(row,col);
        if (IsValidWord(newPrefix)) {
            words.add(newPrefix);
        }

        if (IsValidPrefix(newPrefix)) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = row + i;
                    int newCol = col + j;
                    if (0 <= newRow && newRow < board.rows() && 0 <= newCol && newCol < board.cols() && !marked.get(newRow * board.cols() + newCol)) {
                        getWordsHelper(board, (BitSet)marked.clone(), words, newPrefix, newRow, newCol);
                    }
                }
            }
        }
    }

    private boolean IsValidWord(String string) {
        return (string.length() >= 3 && dictionary.contains(string));
    }

    private boolean IsValidPrefix(String string) {
        return (dictionary.keysWithPrefix(string).iterator().hasNext());
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
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

        long startTime = System.nanoTime();
        int T = 50;
        int M = 4;
        int N = 4;
        for (int i = 0; i < T; i++) {
            BoggleBoard b = new BoggleBoard(M,N);
            solver.getAllValidWords(board);
        }
        long endTime = System.nanoTime();
        StdOut.printf("%g ms per solve, %dx%d board\n", (endTime - startTime) * 1.0 / (1000000*T), M, N);
    }
}
